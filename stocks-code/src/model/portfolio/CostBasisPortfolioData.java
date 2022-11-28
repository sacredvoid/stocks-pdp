package model.portfolio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import model.ModelOrchestratorV2;
import model.apistockops.StockHandler;
import model.costbasis.CostBasisStrategy;
import model.fileops.JSONFileOps;

public class CostBasisPortfolioData extends PortfolioData{

  private CostBasisStrategy costBasisStrategy;

  /**
   * PortfolioData() constructor takes in the stock data list, total invested and total commission for
   * a particular record creates a new PortfolioData object.
   *
   * @param sd              a list of stock data
   * @param totalInvested   the total amount invested by the user
   * @param totalCommission the total amount of commission taken by the application
   * @param totalEarned
   */
  public CostBasisPortfolioData(List<StockData> sd, float totalInvested,
      float totalCommission, float totalEarned, CostBasisStrategy costBasisStrategy ) {
    super(sd, totalInvested, totalCommission, totalEarned);
    this.costBasisStrategy =costBasisStrategy;
  }

  public CostBasisStrategy getCostBasisStrategy() {
    return costBasisStrategy;
  }

  public void setCostBasisStrategy(CostBasisStrategy costBasisStrategy) {
    this.costBasisStrategy = costBasisStrategy;
  }

  public static void main(String args[]) throws IOException {
    String cbsData = new JSONFileOps().readFile("test.json", "PortfolioData");
    CostBasisStrategy cbs = new Gson().fromJson(cbsData, new TypeToken<CostBasisStrategy>() {
    }.getType());

    List<StockData> lStockData = new ArrayList<>();
    float totalInvest  = cbs.getRecurrInvAmt();
    for (Entry<String,Float> e: cbs.getStockPercentMap().entrySet()
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

    CostBasisPortfolioData pfd = new CostBasisPortfolioData(lStockData,totalInvest, 2.0F, 0.0F,cbs);

    new JSONFileOps().writeToFile("testCostBasisPF.json","PortfolioData",pfd.toString());


  }
}
