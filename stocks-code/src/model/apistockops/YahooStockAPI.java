package model.apistockops;

import java.net.URL;
//import model.apistockops.StockHandler.StockHandlerBuilder;

public class YahooStockAPI implements ApiHandler {

  private String stockSymbol;

  private StringBuilder data;
  private URL url;
//  private final String apiKey = "YX2FGEIVWJY89MVU";
//  private final String alphaVantageAPI = "https://www.alphavantage"
//      + ".co/query?function=TIME_SERIES_DAILY"
//      + "&outputsize=full"
//      + "&symbol"
//      + "=";

  private YahooStockAPI(String stockSymbol) {
    this.stockSymbol = stockSymbol;
  }


  static YahooStockApiBuilder getBuilder() {
    return new YahooStockApiBuilder();
  }

  static class YahooStockApiBuilder {

    private String stockSymbol;

    YahooStockApiBuilder stockSymbol(String stockSymbol) {
      this.stockSymbol = stockSymbol;
      return this;
    }


    YahooStockAPI build() {
      return new YahooStockAPI(this.stockSymbol);
    }
  }
  @Override
  public ApiHandler createURL() {
    return null;
  }

  @Override
  public boolean works() {
    return false;
  }

  @Override
  public void writeJson() {
//    return null;
  }

  @Override
  public String getStatus() {
    return null;
  }

  @Override
  public StringBuilder fetch(URL url) {
    return ApiHandler.super.fetch(url);
  }
}
