package model.portfolio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CSVToPortfolioAdapter {
  public static StockData buildStockData(String data) {
    String[] dataSplit = data.split(",");
    // Validate data
    return new StockData(dataSplit[0], Float.parseFloat(dataSplit[1]));
  }

  public static PortfolioData buildPortfolioData(
      List<StockData> stockDataList,
      float totalInvested,
      float totalCommission) {
    return new PortfolioData(stockDataList, totalInvested, totalCommission);
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
      String stockData
  ){
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

      //STOCK TICKER VALIDATION
//      if(!stockQuantity[1].matches(SCRIP_REGEX)){
//        throw new IllegalArgumentException("Make sure to use Valid Stock Ticker for each input.");
//      }
      StockData currentStock = new StockData(stockQuantity[0], Float.parseFloat(stockQuantity[1]));

//      //DATE VALIDATION
//      if(!date.matches(VALID_DATE_REGEX)){
//        throw new IllegalArgumentException("Make sure DATE is in YYYY-MM-DD format for each input.");
//      }

      if(pfData.containsKey(date)) {
        pfData.get(date).addStock(currentStock);
      }

      else {
        List<StockData> tempList = new ArrayList<>();
        tempList.add(currentStock);
        PortfolioData tempP = new PortfolioData(tempList,totalInvested,totalCommission);
        pfData.put(date,tempP);
      }
    }
    return pfData;
  }

}