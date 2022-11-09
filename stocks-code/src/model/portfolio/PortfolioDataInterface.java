package model.portfolio;

import java.util.List;

/**
 * Defines the structure and methods on a Portfolio Object
 */
public interface PortfolioDataInterface {

  List<StockData> getStockList();
  String addStock(StockData newStock);

  float getQuantity(String stockName);
  String setQuantity(String stockName, float quantity);

  float getTotalInvested();

  void setTotalInvested(float newInvested);

  float getTotalCommission();
  void setTotalCommission(float newCommission);
}
