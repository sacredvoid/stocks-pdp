package model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.Port;

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

  public static RequestHandlerBuilder getBuilder(){
    return new RequestHandlerBuilder();
  }
  public static class RequestHandlerBuilder{
    private String stockSymbol;
    public RequestHandlerBuilder stockSymbol(String stockSymbol){
      this.stockSymbol = stockSymbol;
      return this;
    }

    public RequestHandler build(){
      return new RequestHandler(this.stockSymbol);
    }
  }
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
    catch (
  IOException e) {
    throw new IllegalArgumentException("No price data found for "+stockSymbol);
  }
//    new PortfolioWriter()
//        .writeToFile(""+stockSymbol+"Data.csv","StocksData", output.toString());
//    System.out.println("Return value: ");
//    System.out.println(output.toString());
    return output.toString();

  }

  public static void main(String args[]){
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
