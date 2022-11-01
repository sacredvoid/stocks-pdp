package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
/**
 * StockHandler class defines the methods required for fetching the stock data according
 * to the requirement of the user.(either by date or the current value of the stocks.
 * */
public class StockHandler{
  private String name ;
  private String date;

  private StockHandler(String name, String date){
    this.name = name;
    this.date = date;
  }

  /**
   * getBuilder() is a static method which returns a new StockHandlerBuilder object for<p></p>
   * building the StockHandler object.
   * @return a StockHandlerBuilder object
   */
  public static StockHandlerBuilder getBuilder(){
    return new StockHandlerBuilder();
  }

  /**
   * StockHandlerBuilder class is a static class which is used to build the object of<p></p>
   * StockHandler class.
   */
  public static class StockHandlerBuilder{
    private String name;
    private String date = null;

    /**
     * the name() method is a setter method for the name or symbol to the StockHandlerBuilder class.
     * @param name name or symbol of the stock
     * @return the same StockHandlerBuilder object with the stock symbol stored as an attribute.
     */
    public StockHandlerBuilder name(String name){
      this.name = name;
      return this;
    }

    /**
     * the date() method is a setter method for the date to the StockHandlerBuilder class.
     * @param date to retrieve the data fo the specific stock on that day
     * @return the same StockHandlerBuilder object with the date stored as an attribute.
     */
    public StockHandlerBuilder date(String date){
      this.date = date;
      return this;
    }

    /**
     * build() method of the StockHandlerBuilder class creates a new object of the <p></p>
     * StockHandler object with the stock symbol and date.
     * @return new object of the StockHanlder class
     */
    public StockHandler build(){
      return new StockHandler(this.name,this.date);
    }
  }

  /**
   * fetchByDate() fetches the stock data on the specific mentioned date from the API using<p></p>
   * using the RequestHandler object.
   * @return Stock data on that particular date as a String.
   */
  public String fetchByDate() {
    String stockData;
    String dateString = this.date;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime now = LocalDateTime.now();
    Date stockUpdateTime = null;
    Date todayTime = null;
    try{
      stockUpdateTime = sdf.parse("21:00");
    } catch(ParseException e){
      e.printStackTrace();
    }

    String todayDateAndTime = dtf.format(now);
    String todayDate = todayDateAndTime.split(" ")[0];
    try {
      todayTime = sdf.parse(todayDateAndTime.split(" ")[1]);
    } catch(ParseException e){
      //
    }
    String output ="";
    String [] records;


    try{
      stockData = new CSVFileOps().readFile(""+this.name+"Data.csv","StocksData");
      if(!stockData.contains(todayDate) && todayTime.after(stockUpdateTime)){
        stockData = stockDataFetcher(this.name);
      }

    } catch( FileNotFoundException e){

      stockData = this.stockDataFetcher(this.name);
    }

    records = stockData.split("\n");

    if(this.date.equals(todayDate)){
      output+= this.name + "," + records[1].split(",")[4];
    }
    else{
      for (String r: records
      ) {
        if(r.contains(this.date)) {
          String[] sepData = r.split(",");
          output += this.name + "," + sepData[4];
        }
      }
    }
    return output;
  }


  private String stockDataFetcher(String name)  {

    return RequestHandler.getBuilder()
          .stockSymbol(name)
          .build()
          .buildURL()
          .fetch();
  }

  public static void main(String args[]) throws ParseException, FileNotFoundException {
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
    String dateValue = "2022-11-01";
    String valueUsingDate = StockHandler.getBuilder()
        .name("IBM")
        .date(dateValue)
        .build()
        .fetchByDate();
    if( valueUsingDate.equals("")){
      System.out.println("No record of stock exchange on that day");
    }
    else{
      System.out.println(valueUsingDate);
    }

//    String currentValue = StockHandler.getBuilder()
//        .name("IBM")
//        .build()
//        .fetchCurrentValue();

//    System.out.println(currentValue);
  }
}
