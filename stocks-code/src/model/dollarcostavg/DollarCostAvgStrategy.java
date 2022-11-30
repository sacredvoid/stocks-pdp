package model.dollarcostavg;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import model.apistockops.StockHandler;
import model.fileops.JSONFileOps;

public class DollarCostAvgStrategy implements IDollarCostAvg {

  private Map<String,Float> stockPercentMap;
  private float recurrInvAmt;
  private String startDate;
  private String endDate;
  private long recurrCycle;

  public DollarCostAvgStrategy(Map<String, Float> stringFloatMap, float recurrInvAmt, String startDate,
      String endDate, long recurrCycle) {
    this.stockPercentMap = stringFloatMap;
    this.recurrInvAmt = recurrInvAmt;
    this.startDate = startDate;
    this.endDate = endDate;
    this.recurrCycle = recurrCycle;
  }

  public Map<String, Float> getStockPercentMap() {
    return stockPercentMap;
  }

  public void setStockPercentMap(Map<String, Float> stockPercentMap) {
    this.stockPercentMap = stockPercentMap;
  }

  public float getRecurrInvAmt() {
    return recurrInvAmt;
  }

  public void setRecurrInvAmt(float recurrInvAmt) {
    this.recurrInvAmt = recurrInvAmt;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public long getRecurrCycle() {
    return recurrCycle;
  }

  public void setRecurrCycle(long recurrCycle) {
    this.recurrCycle = recurrCycle;
  }



  @Override
  public String dcgStockQtyList(String date, float actualInvestment) {

    List<String> stockQtyList = new ArrayList<>();
    if( this.stockPercentMap.size()==0){
      return "No stocks were included in this strategy";
    }
//    float actualInvestment = this.recurrInvAmt - (commission * stockPercentMap.size());
    for (Entry<String,Float> stockPercent: this.stockPercentMap.entrySet()
    ) {
      String stockName = stockPercent.getKey();
      float percent = stockPercent.getValue();
      double amtPercent = Math.ceil((actualInvestment*percent)/100);
      StockHandler sh = StockHandler.getBuilder().build();
      String dcaStatus = sh.DCAHolidayNextWorkingDay(stockName,date);
      if(dcaStatus.contains("change") || dcaStatus.contains("no further")){
        return dcaStatus;
      }
      float stockPrice = Float.parseFloat(StockHandler.getBuilder()
                                .name(stockName)
                                .date(date)
                                .build()
                                .fetchByDate()
          .split(",")[1]);

      float stockQtyPerPercentage = (float) (amtPercent/stockPrice);
      stockQtyList.add(stockName+","+stockQtyPerPercentage+","+date+",BUY");

    }

    return String.join("\n",stockQtyList);
  }


  public static void main(String args[]) throws FileNotFoundException {

    Gson g = new Gson();
    String data = new JSONFileOps().readFile("test.json", "PortfolioData");
    DollarCostAvgStrategy cbsMap = g.fromJson(data, new TypeToken<DollarCostAvgStrategy>() {
    }.getType());

//    System.out.println(cbsMap.getStockPercentMap());
//    System.out.println(cbsMap.getRecurrCycle());
//    System.out.println(cbsMap.getStartDate());
//    System.out.println(cbsMap.getEndDate());
//    System.out.println(cbsMap.getRecurrInvAmt());

    System.out.println(cbsMap.dcgStockQtyList("2022-11-10", 2.0F));

  }
}
