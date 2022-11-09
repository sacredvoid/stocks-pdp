package model.portfolio;

import java.util.List;

public class PortfolioData implements PortfolioInterface{
  private List<StockData> stockData;
  private float totalInvested;
  private float totalCommission;

  public PortfolioData(List<StockData> sd, float totalInvested, float totalCommission) {
    this.stockData = sd;
    this.totalCommission = totalCommission;
    this.totalInvested = totalInvested;
  }

  @Override
  public List<StockData> getStockList() {
    return stockData;
  }

  @Override
  public String addStock(StockData newStock) {
    try {
      stockData.add(newStock);
    }
    catch (Exception e) {
      return "Adding stock failed: "+e.getMessage();
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

    if(found!=null) {
      return found.getQuantity();
    }
    else return -1;
  }

  @Override
  public String setQuantity(String stockName, float quantity) {
    StockData found = stockData
        .stream()
        .filter(stockScrip -> stockName.equals(stockScrip.getStockName()))
        .findAny()
        .orElse(null);

    if(found!=null) {
      found.setQuantity(quantity);
      return "Successfully changed stock quantity";
    }
    else {
      return "Stock not found";
    }
  }
}
