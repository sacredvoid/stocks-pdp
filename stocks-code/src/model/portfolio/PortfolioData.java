package model.portfolio;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioData implements IPortfolioData {

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
