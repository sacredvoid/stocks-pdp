package model.portfolio;

public class StockData implements StockDataInterface {

  private String stockName;
  private float quantity;

  public StockData(String name, float quantity) {
    this.stockName = name;
    this.quantity = quantity;
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
}
