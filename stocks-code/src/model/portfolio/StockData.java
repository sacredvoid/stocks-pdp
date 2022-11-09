package model.portfolio;

public class StockData {

  private String stockName;
  private float quantity;

  public StockData(String name, float quantity) {
    this.stockName = name;
    this.quantity = quantity;
  }

  public String getStockName() {
    return this.stockName;
  }

  public float getQuantity() {
    return this.quantity;
  }

  public void setQuantity(float quantity) {
    this.quantity = quantity;
  }
}
