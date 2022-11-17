package model.portfolio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSVToPortfolioAdapter class defines methods for converting data from CSV format to Portfolio
 * format.
 */
public class CSVToPortfolioAdapter {

  /**
   * buildStockData() is used to create a StockData object.
   *
   * @param data Stock ticker and quantity as a string
   * @return a new StockData object of the stock ticker and quantity
   */
  public static StockData buildStockData(String data) {
    String[] dataSplit = data.split(",");
    // Validate data
    return new StockData(dataSplit[0], Float.parseFloat(dataSplit[1]));
  }

  /**
   * buildPortfolioData() method creates a new PortfolioData object.
   *
   * @param stockDataList   a List of StockData objects
   * @param totalInvested   total investment amount
   * @param totalCommission total commision amount
   * @return new PortfolioData object
   */
  public static PortfolioData buildPortfolioData(
      List<StockData> stockDataList,
      float totalInvested,
      float totalCommission) {
    return new PortfolioData(stockDataList, totalInvested, totalCommission);
  }

  /**
   * appendPFDataByDate() appends a new record in to the portfolio
   *
   * @param date          on for which a new record is to be entered
   * @param pfData        consisting StockQuntity,commision,investment and costbasis data
   * @param fullPortfolio the complete portfolio that needs to be updated
   * @return the updated fullPortfolio
   */
  public static Map<String, PortfolioData> appendPFDataByDate(
      String date,
      PortfolioData pfData,
      Map<String, PortfolioData> fullPortfolio) {
    // Probably add logic to handle date clashes
    fullPortfolio.put(date, pfData);
    return fullPortfolio;
  }

  /**
   * @param stockData
   * @return
   */
  public static Map<String, PortfolioData> buildPortfolioData(
      String stockData
  ) {
    // Get all dates first, create a set. Iterate again through the data and append
    // stock data by date+commission+totalinvested
    Map<String, PortfolioData> pfData = new HashMap<>();
    String[] dataPerLine = stockData.split("\n");
    for (String line : dataPerLine
    ) {
      String[] stockQuantity = line.split(",");
      String date = stockQuantity[2];
      // validate
      // calculate these two
      float totalInvested = 0;
      float totalCommission = 0;

      StockData currentStock = new StockData(stockQuantity[0], Float.parseFloat(stockQuantity[1]));

      if (pfData.containsKey(date)) {
        pfData.get(date).addStock(currentStock);
      } else {
        List<StockData> tempList = new ArrayList<>();
        tempList.add(currentStock);
        PortfolioData tempP = new PortfolioData(tempList, totalInvested, totalCommission);
        pfData.put(date, tempP);
      }
    }
    return pfData;
  }

}