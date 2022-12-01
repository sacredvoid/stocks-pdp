package model.portfolio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import model.apistockops.StockHandler;
import model.dollarcostavg.DollarCostAvgStrategy;
import model.fileops.JSONFileOps;

public class DollarCostAveragePortfolio extends PortfolioData{

  private Map<String, DollarCostAvgStrategy> dcaStrategy;

  /**
   * PortfolioData() constructor takes in the stock data list, total invested and total commission for
   * a particular record creates a new PortfolioData object.
   *
   * @param sd              a list of stock data
   * @param totalInvested   the total amount invested by the user
   * @param totalCommission the total amount of commission taken by the application
   * @param totalEarned
   */
  public DollarCostAveragePortfolio(List<StockData> sd, float totalInvested,
      float totalCommission, float totalEarned, Map<String, DollarCostAvgStrategy> dcaStrategy) {
    super(sd, totalInvested, totalCommission, totalEarned);
    this.dcaStrategy = dcaStrategy;
  }

  public Map<String, DollarCostAvgStrategy> getDcaStrategy() {
    return dcaStrategy;
  }

  public void setDcaStrategy(Map<String, DollarCostAvgStrategy> dcaStrategy) {
    this.dcaStrategy = dcaStrategy;
  }


  public static Map<String,DollarCostAveragePortfolio> portfolioToDCA(Map<String,PortfolioData> pfData, Map<String,DollarCostAvgStrategy> strategyMap){
    Map<String,DollarCostAveragePortfolio> dcaPortfolio = new HashMap<>();
    for (Entry<String,PortfolioData> entry : pfData.entrySet()
    ) {
      PortfolioData pf = entry.getValue();
      Map<String,DollarCostAvgStrategy> individualStrategyMap = new LinkedHashMap<>();
      for (Entry<String,DollarCostAvgStrategy> singleStrategy: strategyMap.entrySet()
      ) {
        if(entry.getKey().compareTo(singleStrategy.getValue().getStartDate())>=0 &&
        entry.getKey().compareTo(singleStrategy.getValue().getEndDate())<=0){
          individualStrategyMap.put(singleStrategy.getKey(),singleStrategy.getValue());
        }
      }
      DollarCostAveragePortfolio dca = new
          DollarCostAveragePortfolio(pf.getStockList(),pf.getTotalInvested(),pf.getTotalCommission(),pf.getTotalEarned(),individualStrategyMap );
      dcaPortfolio.put(entry.getKey(), dca);
    }
    return dcaPortfolio;
  }

  public static Map<String,PortfolioData> dcaToPortfolio(Map<String,DollarCostAveragePortfolio> dcaData){
    Map<String,PortfolioData> pfData = new HashMap<>();
    for (Entry<String,DollarCostAveragePortfolio> entry : dcaData.entrySet()
    ) {
      DollarCostAveragePortfolio dca = entry.getValue();
      PortfolioData pf = new
          PortfolioData(dca.getStockList(),dca.getTotalInvested(),dca.getTotalCommission(),dca.getTotalEarned());
      pfData.put(entry.getKey(), pf);
    }
    return pfData;
  }

  public static void main(String args[]) throws IOException {
    String cbsData = new JSONFileOps().readFile("test.json", "PortfolioData");
    Map<String, DollarCostAvgStrategy> cbs = new LinkedHashMap<>();
    cbs.put("strategy1",new Gson().fromJson(cbsData, new TypeToken<DollarCostAvgStrategy>() {
    }.getType()));

    DollarCostAvgStrategy cbs1 = cbs.getOrDefault("strategy1",null);
    List<StockData> lStockData = new ArrayList<>();
    float totalInvest  = cbs1.getRecurrInvAmt();
    for (Entry<String,Float> e: cbs1.getStockPercentMap().entrySet()
    ) {
      String stock_name = e.getKey();
      float percentage = e.getValue();
      int amt_per_stock = (int)(totalInvest*percentage)/100;
      String stockPriceString = StockHandler.getBuilder().name(stock_name)
          .date("2022-11-10")
          .build().fetchByDate();
      float value = Float.parseFloat(stockPriceString.split(",")[1]);
      float qty =  amt_per_stock/value;

      lStockData.add(new StockData(stock_name,qty));

//      StockData sd = new StockData(stock_name,nttotalInvest/)
    }

    DollarCostAveragePortfolio pfd = new DollarCostAveragePortfolio(lStockData,totalInvest, 2.0F, 0.0F,cbs);

    new JSONFileOps().writeToFile("testCostBasisPF.json","PortfolioData",pfd.toString());


  }
}
