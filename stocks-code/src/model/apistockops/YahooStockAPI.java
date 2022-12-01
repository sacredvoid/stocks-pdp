package model.apistockops;

import java.net.URL;

/**
 * The type Yahoo stock api.
 */
public class YahooStockAPI implements ApiHandler {

  private StringBuilder data;
  private URL url;

  private YahooStockAPI(String stockSymbol) {
  }


  /**
   * Gets builder.
   *
   * @return the builder
   */
  static YahooStockApiBuilder getBuilder() {
    return new YahooStockApiBuilder();
  }

  /**
   * The type Yahoo stock api builder.
   */
  static class YahooStockApiBuilder {

    private String stockSymbol;

    /**
     * Stock symbol yahoo stock api builder.
     *
     * @param stockSymbol the stock symbol
     * @return the yahoo stock api builder
     */
    YahooStockApiBuilder stockSymbol(String stockSymbol) {
      this.stockSymbol = stockSymbol;
      return this;
    }


    /**
     * Build yahoo stock api.
     *
     * @return the yahoo stock api
     */
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
    // return null
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
