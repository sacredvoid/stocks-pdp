package model.portfolio;

import com.google.gson.Gson;
import java.util.List;

public class PortfolioData implements IPortfolioData, Cloneable{

  private List<StockData> stockData;
  private float totalInvested;
  private float totalCommission;

  private float totalEarned;

  public PortfolioData(List<StockData> sd, float totalInvested, float totalCommission,
      float totalEarned) {
    this.stockData = sd;
    this.totalCommission = totalCommission;
    this.totalInvested = totalInvested;
    this.totalEarned = totalEarned;
  }

//  @Override
//  public Object clone() throws CloneNotSupportedException {
//    PortfolioData clone = null;
//    try {
//      clone = (PortfolioData) super.clone();
//
//      clone.setStockList((List<StockData>) this.stockData.clone());
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
  public float getTotalInvested() {
    return this.totalInvested;
  }

  @Override
  public void setTotalInvested(float newInvested) {
    this.totalInvested = newInvested;
  }

  @Override
  public float getTotalCommission() {
    return this.totalCommission;
  }

  @Override
  public void setTotalCommission(float newCommission) {
    this.totalCommission = newCommission;
  }

  @Override
  public float getTotalEarned() {
    return this.totalEarned;
  }

  @Override
  public void setTotalEarned(float newEarning) {
    this.totalEarned = newEarning;
  }


  @Override
  public List<StockData> getStockList() {
    return stockData;
  }

  @Override
  public String addStock(StockData newStock) {
    try {
      for (StockData s : stockData) {
        if (s.equals(newStock)) {
          s.setQuantity(newStock.getQuantity()+s.getQuantity());
          return "Adding successful";
        }
      }
      stockData.add(newStock);
    } catch (Exception e) {
      return "Adding stock failed: " + e.getMessage();
    }
    return "Adding successful";
  }

  @Override
  public String removeStock(StockData newStock) {
    try {
      for (StockData s : stockData) {
        if (s.equals(newStock)) {
          s.setQuantity(s.getQuantity()-newStock.getQuantity());
          return "Adding successful";
        }
      }
      stockData.add(newStock);
    } catch (Exception e) {
      return "Removing stock failed: " + e.getMessage();
    }
    return "Removing successful";
  }

  @Override
  public void setStockList(List<StockData> newStockList) {
    this.stockData = newStockList;
  }

  @Override
  public float getQuantity(String stockName) {
    StockData found = stockData
        .stream()
        .filter(stockScrip -> stockName.equals(stockScrip.getStockName()))
        .findAny()
        .orElse(null);

    if (found != null) {
      return found.getQuantity();
    } else {
      return -1;
    }
  }

  @Override
  public String setQuantity(String stockName, float quantity) {
    StockData found = stockData
        .stream()
        .filter(stockScrip -> stockName.equals(stockScrip.getStockName()))
        .findAny()
        .orElse(null);

    if (found != null) {
      found.setQuantity(quantity);
      return "Successfully changed stock quantity";
    } else {
      return "Stock not found";
    }
  }
}
