package model.portfolio;

import java.util.List;
import java.util.Map;
import model.apistockops.PortfolioValue;

/**
 * CSVToPortfolioAdapter class defines methods for converting data from CSV format to Portfolio
 * format.
 */
public class CSVToPortfolioAdapter {

  /**
   * appendPFDataByDate() appends a new record in to the portfolio.
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
    fullPortfolio.put(date, pfData);
    return fullPortfolio;
  }

  /**
   * Build portfolio data map.
   *
   * @param stockData      the stock data
   * @param pfData         the pf data
   * @param commissionFees the commission fees
   * @return the map
   */
  public static <T extends PortfolioData> Map<String, T> buildPortfolioData(
      String stockData, Map<String, T> pfData, float commissionFees
  ) {
    String[] dataPerLine = stockData.split("\n");
    for (String line : dataPerLine
    ) {
      String[] stockQuantity = line.split(",");
      String date = stockQuantity[2];
      float totalTransaction;
      float totalCommission = commissionFees;

      List<String> portfolioValue = PortfolioValue.getBuilder()
          .stockCountList(line)
          .date(date)
          .build()
          .completePortfolioValue();

      totalTransaction = Float.parseFloat(portfolioValue.get(1).split(",")[2]);

      StockData currentStock = new StockData(stockQuantity[0], Float.parseFloat(stockQuantity[1]));
      CascadeTransactions.updatePortfolio(stockQuantity[3], pfData, currentStock, date,
          totalTransaction, totalCommission);
    }
    return pfData;
  }

}