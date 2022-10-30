package model;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
  public String fetchByDate(){
//    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//    String dateString = dateFormat.format(this.date);
    String dateString = this.date;
    String stockData = stockDataFetcher(this.name);
    String [] records = stockData.split("\n");

    String output ="";
    for (String x : records
    ) {
      if (x.contains(dateString)) {
        String[] sepData = x.split(",");
        output += this.name + "," + sepData[4];
        break;
      }
    }
    return output;
  }

  /**
   * fetchCurrentValue() method returns the current stock data using the RequestHandler Object.
   * @return current data of the stock as a String
   */
  public String fetchCurrentValue(){
    String output ="";
    String stockData = stockDataFetcher(this.name);
//    String [] records = stockData.split(System.lineSeparator());
    String [] records = stockData.split("\n");
    String [] sepData = records[1].split(",");
    output += this.name + "," + sepData[4];
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
    String dateValue = "2022-09-30";
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

    String currentValue = StockHandler.getBuilder()
        .name("IBM")
        .build()
        .fetchCurrentValue();

    System.out.println(currentValue);
  }
}
