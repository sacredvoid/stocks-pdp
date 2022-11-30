package model.apistockops;

import com.google.gson.internal.LinkedTreeMap;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import model.apidata.ApiDataAdapter;
import model.apidata.ApiDataStruct;
import model.fileops.JSONFileOps;
import model.portfolio.PortfolioData;
import model.portfolio.filters.DateAfterPredicate;
import model.portfolio.filters.DateBeforePredicate;
import model.validation.DateValidator;


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
    SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime now = LocalDateTime.now();
    Date stockUpdateTime = null;
    Date todayTime = null;
    Date requiredDate = null;
    Date todayDateObj = null;
    Date requiredDateObj = null;
    String todayDateAndTime = dtf.format(now);
    String todayDate = todayDateAndTime.split(" ")[0];

    try {
      stockUpdateTime = sdf.parse(todayDate + " 17:00");
      todayTime = sdf.parse(todayDateAndTime);
      todayDateObj = dateSdf.parse(todayDate);
      requiredDate = sdf.parse(this.date + " 23:59");
      requiredDateObj = dateSdf.parse(this.date);
    } catch (ParseException ignored) {
      //
    }

    String output = "";
    stockData = stockDateReader(this.name);
    if (!stockData.containsKey(this.date) && todayTime.after(stockUpdateTime)) {
        stockData = stockDataFetcher(this.name);
      }

    if (stockData == null) {
      output = this.status;
      return output;
    }

    ApiDataStruct stockInfo = stockData.getOrDefault(this.date, null);
    if (stockInfo == null) {
//      if (!requiredDate.after(todayTime) || todayTime.compareTo(requiredDate) < 0) {
//        return fetchCurrent(stockData);
//      }
      if(requiredDate.after(todayTime)){
        return fetchCurrent(stockData);
      }else {
        if (todayDateObj.compareTo(requiredDateObj) == 0) {
          return fetchCurrent(stockData);
        } else {
          List<String> recentDatelist = dateBeforeList(this.date, stockData);
          this.date = recentDatelist.get(recentDatelist.size() - 1);
          return fetchByDate();
        }
      }
//      return "no data found";
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

  private List<String> dateAfterList(String date, Map<String,ApiDataStruct> stockInfo){
    DateAfterPredicate dateAfterPredicate = new DateAfterPredicate(date);
    TreeMap<String, ApiDataStruct> filterdData = new TreeMap<>();
    stockInfo.entrySet().stream().filter(entry-> dateAfterPredicate.test(entry.getKey()))
        .forEach(entry-> filterdData.put(entry.getKey(), entry.getValue()));
    List<String> dateList = new ArrayList<>(filterdData.keySet());

    return dateList;
  }

  private List<String> dateBeforeList(String date, Map<String,ApiDataStruct> stockInfo){
    DateBeforePredicate dateBeforePredicate = new DateBeforePredicate(date);
    TreeMap<String, ApiDataStruct> filterdData = new TreeMap<>();
    stockInfo.entrySet().stream().filter(entry-> dateBeforePredicate.test(entry.getKey()))
        .forEach(entry-> filterdData.put(entry.getKey(), entry.getValue()));
    List<String> dateList = new ArrayList<>(filterdData.keySet());

    return dateList;
  }
  public String DCAHolidayNextWorkingDay(String name,String date){
    Map<String, ApiDataStruct> stockInfo = stockDateReader(name);
    DateAfterPredicate dateAfterPredicate ;
    if(stockInfo.getOrDefault(date,null) == null){

      List<String> dateList = dateAfterList(date,stockInfo);
      if(dateList.size()==0){
        return "no further dates";
      }
      return "change date to "+dateList.get(0);
    }
    return date;

  }
  public Map<String,ApiDataStruct> stockDateReader(String name){
    Map<String,ApiDataStruct> stockData;
    try {

      stockData = ApiDataAdapter.getApiObject(
          new JSONFileOps().readFile(name + "Data.json", "StocksJsonData"));
//      if (!stockData.containsKey(date) && todayTime.after(stockUpdateTime)) {
//        stockData = stockDataFetcher(name);
//      }
    } catch (FileNotFoundException e) {
      stockData = stockDataFetcher(name);
    }
    return stockData;
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


