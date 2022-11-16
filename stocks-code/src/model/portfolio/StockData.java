package model.portfolio;

import com.google.gson.Gson;

public class StockData implements IStockData, Cloneable {

  private String stockName;
  private float quantity;

  public StockData(String name, float quantity) {
    this.stockName = name;
    this.quantity = quantity;
  }

//  @Override
//  public Object clone() throws CloneNotSupportedException {
//    StockData clone = null;
//    try {
//      clone = (StockData) super.clone();
//    }
//    catch (CloneNotSupportedException e) {
//      throw new RuntimeException(e);
//    }
//    return clone;
//  }

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
