package model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.Port;

/**
 * RequestHandler class is responsible for creating requests and fetching the data from the<p></p>
 * the Alphavantage API.
 */
public class RequestHandler {
//  private String apiKey = "SK3WEKBCG40DZ73N"; sam's api key
//  private String apiKey = "MDK9ZTZLD3PS0N5K";

  private String apiKey = "W0M1JOKC82EZEQA8";
//  String stockSymbol = "GOOG"; //ticker symbol for Google
  private String stockSymbol;
  URL url = null;


  private RequestHandler(String stockSymbol){
    this.stockSymbol = stockSymbol;
  }

  /**
   * getBuilder() is a static method which returns a new RequestHandlerBuilder object for<p></p>
   * building the RequestHandler object.
   * @return an RequestHandlerBuilder object
   */
  public static RequestHandlerBuilder getBuilder(){
    return new RequestHandlerBuilder();
  }

  /**
   * RequestHandlerBuilder class is a static class which is used to build the object of<p></p>
   * RequestHandler class.
   */
  public static class RequestHandlerBuilder{
    private String stockSymbol;

    /**
     * the stockSymbol method is a setter method for the stockSymbol to the<p></p>
     * RequestHandlerBuilder class.
     * @param stockSymbol name or symbol of the stock
     * @return the same RequestHandlerBuilder object with the stock symbol stored as an attribute.
     */
    public RequestHandlerBuilder stockSymbol(String stockSymbol){
      this.stockSymbol = stockSymbol;
      return this;
    }

    /**
     * build() method of the RequestHandlerBuilder class creates a new object of the <p></p>
     * RequestHandler object with the stock symbol.
     * @return new object of the RequestHandler class
     */
    public RequestHandler build(){
      return new RequestHandler(this.stockSymbol);
    }
  }

  /**
   * buildURL() method creates the URL for the API request using the name and symbol <p></p>
   * of the stock passed to it.
   * @return the same RequestHandler object with url built and stored as an attribute
   */
  public RequestHandler buildURL() {

    try {
      /*
      create the URL. This is the query to the web service. The query string
      includes the type of query (DAILY stock prices), stock symbol to be
      looked up, the API key and the format of the returned
      data (comma-separated values:csv). This service also supports JSON
      which you are welcome to use.
       */

      url = new URL("https://www.alphavantage"
            + ".co/query?function=TIME_SERIES_DAILY"
            + "&outputsize=full"
            + "&symbol"
            + "=" + stockSymbol + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
          + "no longer works");
    }
    return this;
  }

  /**
   * fetch() method fetches the data from the API using the URL.
   * @return Stock data in the form of String
   */
  public String fetch(){
  InputStream in = null;
  StringBuilder output = new StringBuilder();

    try {
      /*
      Execute this query. This returns an InputStream object.
      In the csv format, it returns several lines, each line being separated
      by commas. Each line contains the date, price at opening time, highest
      price for that date, lowest price for that date, price at closing time
      and the volume of trade (no. of shares bought/sold) on that date.

      This is printed below.
       */
      in = url.openStream();
      int b;

      while ((b=in.read())!=-1) {
        output.append((char)b);
      }
    }
    catch (IOException e) {
      throw new IllegalArgumentException("No price data found for "+stockSymbol);
    }


    try {
      CSVFileOps f = new CSVFileOps();
      f.writeToFile("" + stockSymbol + "Data.csv", "StocksData", output.toString());
    } catch(IOException e){
      e.printStackTrace();
    }
    return output.toString();

  }

  public static void main(String args[]) throws IOException{
//    RequestHandler r = new RequestHandler();
//    r.buildURL("GOOG");
//    r.fetchAndSave();
    System.out.println("Feteched and Saved this Data \n"+RequestHandler.getBuilder()
        .stockSymbol("GOOG")
        .build()
        .buildURL()
        .fetch());
  }
}
