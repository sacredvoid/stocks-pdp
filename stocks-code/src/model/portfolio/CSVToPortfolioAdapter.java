package model.portfolio;

import java.util.List;
import java.util.Map;
import model.PortfolioValue;

public class CSVToPortfolioAdapter {
  public static StockData buildStockData(String data) {
    String[] dataSplit = data.split(",");
    // Validate data
    return new StockData(dataSplit[0], Float.parseFloat(dataSplit[1]));
  }

  public static PortfolioData buildPortfolioData(
      List<StockData> stockDataList,
      float totalInvested,
      float totalCommission,
      float totalEarned) {
    return new PortfolioData(stockDataList, totalInvested, totalCommission, totalEarned);
  }

  public static Map<String, PortfolioData> appendPFDataByDate(
      String date,
      PortfolioData pfData,
      Map<String, PortfolioData> fullPortfolio) {
    // Probably add logic to handle date clashes
    fullPortfolio.put(date,pfData);
    return fullPortfolio;
  }

  public static Map<String, PortfolioData> buildPortfolioData(
      String stockData, Map<String, PortfolioData> pfData
  ){
    // Get all dates first, create a set. Iterate again through the data and append
    // stock data by date+commission+totalinvested
    String[] dataPerLine = stockData.split("\n");
    for (String line : dataPerLine
    ) {
      String[] stockQuantity = line.split(",");
      String date = stockQuantity[2];
      // validate
      // calculate these two
      float totalTransaction;
      float totalCommission = 1;
      float totalEarned = 0;

      List<String> portfolioValue = PortfolioValue.getBuilder()
          .stockCountList(line)
          .date(date)
          .build()
          .completePortfolioValue();

      totalTransaction = Float.parseFloat(portfolioValue.get(1).split(",")[2]);

      StockData currentStock = new StockData(stockQuantity[0], Float.parseFloat(stockQuantity[1]));
      CascadeV2.updatePortfolio(stockQuantity[3], pfData, currentStock, date,
          totalTransaction, totalCommission);
    }
    return pfData;
  }

}