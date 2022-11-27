package model.costbasis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.util.Map;
import model.fileops.JSONFileOps;

public class CostBasisStrategy implements ICostBasis{

  Map<String,Float> stockPercentMap;
  float recurrInvAmt;
  String startDate;
  String endDate;
  long recurrCycle;

  public CostBasisStrategy(Map<String, Float> stringFloatMap, float recurrInvAmt, String startDate,
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

  public static void main(String args[]) throws FileNotFoundException {

    Gson g = new Gson();
    String data = new JSONFileOps().readFile("test.json", "PortfolioData");
    CostBasisStrategy cbsMap = g.fromJson(data, new TypeToken<CostBasisStrategy>() {
    }.getType());

    System.out.println(cbsMap.getStockPercentMap());
    System.out.println(cbsMap.getRecurrCycle());
    System.out.println(cbsMap.getStartDate());
    System.out.println(cbsMap.getEndDate());
    System.out.println(cbsMap.getRecurrInvAmt());

  }
}
