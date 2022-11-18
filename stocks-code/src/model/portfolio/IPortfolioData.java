package model.portfolio;

import java.util.List;

/**
 * IPortfolioData interface defines the method signatures to perform various operations on a
 * StockData object.
 */
public interface IPortfolioData {

  /**
   * getStockList() is used to get the list of stock data.
   *
   * @return StockData objects as a List
   */
  List<StockData> getStockList();

  /**
   * addStock is used to add a new StockData to a record of the portfolio.
   *
   * @param newStock the StockData object
   * @return a status message of the operation as a string
   */
  String addStock(StockData newStock);

  /**
   * getQuantity() returns the quantity of a stock from a portfolio.
   *
   * @param stockName stock ticker
   * @return the quantity of respective stock.
   */
  float getQuantity(String stockName);

  String removeStock(StockData newStock);

  void setStockList(List<StockData> newStockList);

  /**
   * setQuantity() sets the quantity of a stock in a portfolio.
   *
   * @param stockName stock ticker
   * @param quantity  quantity of stock
   * @return returns as status message of the operation as a string
   */
  String setQuantity(String stockName, float quantity);

  /**
   * getTotalInvested() is used to retrieve the total investment on a particular day of a
   * portfolio.
   *
   * @return amount invested as float
   */
  float getTotalInvested();

  /**
   * setTotalInvested() is used to set the investment amount for a particular day of a portfolio.
   *
   * @param newInvested amount invested
   */
  void setTotalInvested(float newInvested);

  /**
   * getTotalCommission() is used to retrieve the total commission on a particular day of a
   * portfolio.
   *
   * @return commission amount as float
   */
  float getTotalCommission();

  /**
   * setTotalCommission() is used to set the commission amount for a particular day of a portfolio.
   *
   * @param newCommission commission amount
   */
  void setTotalCommission(float newCommission);

  float getTotalEarned();

  void setTotalEarned(float newEarning);
}
