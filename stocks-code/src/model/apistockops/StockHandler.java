package model.apistockops;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import model.apidata.ApiDataAdapter;
import model.apidata.ApiDataStruct;
import model.fileops.JSONFileOps;


/**
 * StockHandler class defines the methods required for fetching the stock data according to the
 * requirement of the user on a passed particular date.
 */
public class StockHandler {

  private String name;
  private String date;

  private String staticStockDataDir = "StocksData";

  private String status;


  private StockHandler(String name, String date) {
    this.name = name;
    this.date = date;
  }

  /**
   * getBuilder() is a static method which returns a new StockHandlerBuilder object for<p></p>
   * building the StockHandler object.
   *
   * @return a StockHandlerBuilder object
   */
  public static StockHandlerBuilder getBuilder() {
    return new StockHandlerBuilder();
  }

  /**
   * StockHandlerBuilder class is a static class which is used to build the object of<p></p>
   * StockHandler class.
   */
  public static class StockHandlerBuilder {

    private String name;
    private String date = null;

    /**
     * the name() method is a setter method for the name or symbol to the StockHandlerBuilder
     * class.
     *
     * @param name name or symbol of the stock
     * @return the same StockHandlerBuilder object with the stock symbol stored as an attribute.
     */
    public StockHandlerBuilder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * the date() method is a setter method for the date to the StockHandlerBuilder class.
     *
     * @param date to retrieve the data fo the specific stock on that day
     * @return the same StockHandlerBuilder object with the date stored as an attribute.
     */
    public StockHandlerBuilder date(String date) {
      this.date = date;
      return this;
    }

    /**
     * build() method of the StockHandlerBuilder class creates a new object of the <p></p>
     * StockHandler object with the stock symbol and date.
     *
     * @return new object of the StockHanlder class
     */
    public StockHandler build() {
      return new StockHandler(this.name, this.date);
    }
  }

  /**
   * fetchByDate() fetches the stock data on the specific mentioned date from the API using<p></p>
   * using the RequestHandler object.
   *
   * @return Stock data on that particular date as a String.
   */
  public String fetchByDate() {
    Map<String, ApiDataStruct> stockData;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime now = LocalDateTime.now();
    Date stockUpdateTime = null;
    Date todayTime = null;
    Date requiredDate = null;

    String todayDateAndTime = dtf.format(now);
    String todayDate = todayDateAndTime.split(" ")[0];

    try {
      stockUpdateTime = sdf.parse(todayDate + " 17:00");
      todayTime = sdf.parse(todayDateAndTime);
      requiredDate = sdf.parse(this.date + " 23:59");
    } catch (ParseException ignored) {
      //
    }
    String output = "";

    try {

      stockData = ApiDataAdapter.getApiObject(
          new JSONFileOps().readFile(this.name + "Data.json", "StocksJsonData"));
      if (!stockData.containsKey(this.date) && todayTime.after(stockUpdateTime)) {
        stockData = stockDataFetcher(this.name);
      }
    } catch (FileNotFoundException e) {
      stockData = stockDataFetcher(this.name);
    }

    if (stockData == null) {
      output = this.status;
      return output;
    }

    ApiDataStruct stockInfo = stockData.getOrDefault(this.date, null);
    if (stockInfo == null) {
      if (!requiredDate.after(todayTime) || todayTime.compareTo(requiredDate) < 0) {
        return fetchCurrent(stockData);
      }
      return "no data found";
    }
    output += this.name + "," + stockInfo.getClose();
    return output;
  }

  private String fetchCurrent(Map<String, ApiDataStruct> stockData) {

    String output = "";
    String recentDate = "";
    Optional<String> recentDateKey = stockData.keySet().stream().findFirst();
    if (recentDateKey.isPresent()) {
      recentDate = recentDateKey.get();
    }
    ApiDataStruct stockInfo = stockData.getOrDefault(recentDate, null);
    if (stockInfo == null) {
      return "no data found";
    }
    output += this.name + "," + stockInfo.getClose();
    return output;

  }

  private Map<String, ApiDataStruct> stockDataFetcher(String name) {

    RequestHandler rob = RequestHandler.getBuilder()
        .stockSymbol(name)
        .build();
    Map<String, ApiDataStruct> stockData = rob.buildURL().fetch();
    status = rob.getStatus();
    return stockData;
  }
}


