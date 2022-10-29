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
  private Date date;

  private StockHandler(String name, Date date){
    this.name = name;
    this.date = date;
  }

//  private StockHandler(String name){
//    this.name = name;
//  }
  public static StockHandlerBuilder getBuilder(){
    return new StockHandlerBuilder();
  }


  public static class StockHandlerBuilder{
    private String name;
    private Date date = null;

    public StockHandlerBuilder name(String name){
      this.name = name;
      return this;
    }

    public StockHandlerBuilder date(Date date){
      this.date = date;
      return this;
    }

    public StockHandler build(){
      return new StockHandler(this.name,this.date);
    }
  }

  public String fetchByDate(){
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    String dateString = dateFormat.format(this.date);
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
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
    String valueUsingDate = StockHandler.getBuilder()
        .name("IBM")
        .date(formatter.parse("2022-09-30"))
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
