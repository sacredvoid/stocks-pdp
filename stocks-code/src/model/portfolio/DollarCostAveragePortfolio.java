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

  private Map<String, DollarCostAvgStrategy> costBasisStrategy;

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
      float totalCommission, float totalEarned, Map<String, DollarCostAvgStrategy> costBasisStrategy ) {
    super(sd, totalInvested, totalCommission, totalEarned);
    this.costBasisStrategy =costBasisStrategy;
  }

  public Map<String, DollarCostAvgStrategy> getCostBasisStrategy() {
    return costBasisStrategy;
  }

  public void setCostBasisStrategy(Map<String, DollarCostAvgStrategy> costBasisStrategy) {
    this.costBasisStrategy = costBasisStrategy;
  }


  public static Map<String,DollarCostAveragePortfolio> portfolioToDCA(Map<String,PortfolioData> pfData, Map<String,DollarCostAvgStrategy> strategyMap){
    Map<String,DollarCostAveragePortfolio> dcaPortfolio = new HashMap<>();
    for (Entry<String,PortfolioData> entry : pfData.entrySet()
    ) {
      PortfolioData pf = entry.getValue();
      DollarCostAveragePortfolio dca = new
          DollarCostAveragePortfolio(pf.getStockList(),pf.getTotalInvested(),pf.getTotalCommission(),pf.getTotalEarned(),strategyMap);
      dcaPortfolio.put(entry.getKey(), dca);
    }
    return dcaPortfolio;
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