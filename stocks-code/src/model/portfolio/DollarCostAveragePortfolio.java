package model.portfolio;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import model.dollarcostavg.DollarCostAvgStrategy;

/**
 * The DollarCostAveragePortfolio class which holds data of a strategic (DCA) portfolio and extends
 * the existing portfolio data class.
 */
public class DollarCostAveragePortfolio extends PortfolioData {

  private Map<String, DollarCostAvgStrategy> dcaStrategy;

  /**
   * PortfolioData() constructor takes in the stock data list, total invested and total commission
   * for a particular record creates a new PortfolioData object.
   *
   * @param sd              a list of stock data
   * @param totalInvested   the total amount invested by the user
   * @param totalCommission the total amount of commission taken by the application
   * @param totalEarned     this is the total amount earned by selling stocks in a portfolio
   * @param dcaStrategy     the dca strategy
   */
  public DollarCostAveragePortfolio(List<StockData> sd, float totalInvested,
      float totalCommission, float totalEarned, Map<String, DollarCostAvgStrategy> dcaStrategy) {
    super(sd, totalInvested, totalCommission, totalEarned);
    this.dcaStrategy = dcaStrategy;
  }

  /**
   * Gets dca strategy map.
   *
   * @return the dca strategy
   */
  public Map<String, DollarCostAvgStrategy> getDcaStrategy() {
    return dcaStrategy;
  }

  /**
   * Sets dca strategy.
   *
   * @param dcaStrategy the dca strategy
   */
  public void setDcaStrategy(Map<String, DollarCostAvgStrategy> dcaStrategy) {
    this.dcaStrategy = dcaStrategy;
  }


  /**
   * Method to convert a normal Portfolio to a Strategic (DCA) portfolio.
   *
   * @param <T>         the type parameter
   * @param pfData      the pf data
   * @param strategyMap the strategy map
   * @return the map
   */
  public static <T extends PortfolioData> Map<String, T> portfolioToDCA(Map<String, T> pfData,
      Map<String, DollarCostAvgStrategy> strategyMap) {
    Map<String, T> dcaPortfolio = new HashMap<>();
    for (Entry<String, T> entry : pfData.entrySet()
    ) {
      PortfolioData pf = entry.getValue();
      Map<String, DollarCostAvgStrategy> individualStrategyMap = new LinkedHashMap<>();
      for (Entry<String, DollarCostAvgStrategy> singleStrategy : strategyMap.entrySet()
      ) {
        if (entry.getKey().compareTo(singleStrategy.getValue().getStartDate()) >= 0 &&
            entry.getKey().compareTo(singleStrategy.getValue().getEndDate()) <= 0) {
          individualStrategyMap.put(singleStrategy.getKey(), singleStrategy.getValue());
        }
      }
      @SuppressWarnings("unchecked") T dca = (T) new
          DollarCostAveragePortfolio(pf.getStockList(), pf.getTotalInvested(),
          pf.getTotalCommission(), pf.getTotalEarned(), individualStrategyMap);
      dcaPortfolio.put(entry.getKey(), dca);
    }
    return dcaPortfolio;
  }

  /**
   * Converts a DCA portfolio to a normal portfolio
   *
   * @param <T>     the type parameter of type PortfolioData (the pf data basically)
   * @param dcaData the dca data
   * @return the new PortfolioData map
   */
  public static <T extends PortfolioData> Map<String, T> dcaToPortfolio(Map<String, T> dcaData) {
    Map<String, T> pfData = new HashMap<>();
    for (Entry<String, T> entry : dcaData.entrySet()
    ) {
      T dca = entry.getValue();
      T pf = (T) new
          PortfolioData(dca.getStockList(), dca.getTotalInvested(), dca.getTotalCommission(),
          dca.getTotalEarned());
      pfData.put(entry.getKey(), pf);
    }
    return pfData;
  }
}
