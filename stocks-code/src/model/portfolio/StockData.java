package model.portfolio;

import com.google.gson.Gson;

/**
 * StockData class defines the structure for the stock data that is stored in the portfolio.
 */
public class StockData implements IStockData {


  private String stockName;
  private float quantity;

  /**
   * StockData() constructor takes in name and quantity of the stock ticker and creates the
   * StockData object of that stock ticker.
   *
   * @param name     Stock ticker
   * @param quantity Quantity of the stock
   */
  public StockData(String name, float quantity) {
    this.stockName = name;
    this.quantity = quantity;
  }


  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  @Override
  public String getStockName() {
    return this.stockName;
  }

  @Override
  public float getQuantity() {
    return this.quantity;
  }

  @Override
  public void setQuantity(float quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof StockData)) {
      return false;
    }
    return stockName.equals(((StockData) obj).stockName);
  }

  @Override
  public int hashCode() {
    return 15*stockName.hashCode();
  }

}
