package model.portfolio;

/**
 * IStockData interface defines the method signatures required to perform various operations on
 * StockData objects.
 */
public interface IStockData {

  /**
   * getStockName() method returns the name of a Stock from the StockData object.
   *
   * @return stock ticker as a string
   */
  String getStockName();

  /**
   * getQuantity() method return the quantity of a Stock from the StockData object.
   *
   * @return stock quantity as float
   */
  float getQuantity();

  /**
   * setQuantity() method sets the quantity of a Stock in a StockData object.
   *
   * @param quantity stock quantity as float
   */
  void setQuantity(float quantity);
}
