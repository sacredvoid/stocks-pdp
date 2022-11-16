package model.portfolio;

import java.util.List;


public class PortfolioToCSVAdapter {

  public static String buildStockQuantity(StockData s) {
    StringBuilder stockCSV = new StringBuilder();
    stockCSV.append(s.getStockName()).append(",").append(s.getQuantity());
    return stockCSV.toString();
  }

  public static String buildStockQuantityList(List<StockData> ls) {
    StringBuilder sqlist = new StringBuilder();
    for (StockData s : ls
    ) {
      sqlist.append(buildStockQuantity(s)).append("\n");
    }
    return sqlist.toString().strip();
  }

}
