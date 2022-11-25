package model.portfolio;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import model.apistockops.PortfolioValue;
import model.validation.DateValidator;

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
    // Probably add logic to handle date clashes
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
  public static Map<String, PortfolioData> buildPortfolioData(
      String stockData, Map<String, PortfolioData> pfData, float commissionFees
  ) {
    // Get all dates first, create a set. Iterate again through the data and append
    // stock data by date+commission+totalinvested
    DateValidator dateCheck = new DateValidator();
    String[] dataPerLine = stockData.split("\n");
    for (String line : dataPerLine
    ) {
      String[] stockQuantity = line.split(",");
      String date = stockQuantity[2];
      // validate
      try {
        if (!dateCheck.checkData(date)) {
          // Skip weekends and future dates
          continue;
        }
      } catch (ParseException e) {
        // Invalid date format, skip
        continue;
      }
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