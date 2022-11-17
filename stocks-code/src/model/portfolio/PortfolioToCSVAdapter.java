package model.portfolio;

import java.util.List;

/**
 * PortfolioToCSVAdapter class defines methods for converting data from Portfolio format to CSV
 * format.
 */
public class PortfolioToCSVAdapter {

  /**
   * buildStockQuantity() method returns the stock and quantity entries from the StockData object as
   * a single string.
   *
   * @param s StockData object which consists the stock quantity list
   * @return stock and quantity list as a single string
   */
  public static String buildStockQuantity(StockData s) {
    StringBuilder stockCSV = new StringBuilder();
    stockCSV.append(s.getStockName()).append(",").append(s.getQuantity());
    return stockCSV.toString();
  }

  /**
   * buildStockQuantityList() method return all the stock and quantity entries for each StockData
   * from the List of StockData objects as a single strings.
   *
   * @param ls List of StockData objects
   * @return stock and quantity of each StockData from the list as single string
   */
  public static String buildStockQuantityList(List<StockData> ls) {
    StringBuilder sqlist = new StringBuilder();
    for (StockData s : ls) {
      sqlist.append(buildStockQuantity(s)).append("\n");
    }
    return sqlist.toString().strip();
  }

}
