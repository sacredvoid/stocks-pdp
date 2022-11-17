package model;

import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.fileops.JSONFileOps;

/**
 * AplphaVantageAPI class defines and implements API operations for the AlphaVantageAPI.
 */
class AlphaVantageAPI implements ApiHandler {

  private String stockSymbol;

  private StringBuilder data;
  private URL url;
  private final String apiKey = "YX2FGEIVWJY89MVU";
  private final String alphaVantageAPI = "https://www.alphavantage"
      + ".co/query?function=TIME_SERIES_DAILY"
      + "&outputsize=full"
      + "&symbol"
      + "=";


  private String status = "success";

  private AlphaVantageAPI(String stockSymbol) {
    this.stockSymbol = stockSymbol;
  }


  /**
   * getBuilder() method return a new AlphaVantageApiBuilder object which is used for building the
   * AlphaVantageAPI object
   *
   * @return
   */
  static AlphaVantageApiBuilder getBuilder() {
    return new AlphaVantageApiBuilder();
  }

  /**
   * AlphaVantageApiBuilder class is a static class which is used to build the object of<p></p>
   * AlphaVantageAPI class.
   */
  static class AlphaVantageApiBuilder {

    private String stockSymbol;

    /**
     * stockSymbol() methods is setter method for stock symbol.
     *
     * @param stockSymbol the stock ticker
     * @return the same AlphaVantageApiBuilder object after setting the stock symbol
     */
    AlphaVantageApiBuilder stockSymbol(String stockSymbol) {
      this.stockSymbol = stockSymbol;
      return this;
    }

    /**
     * build() method creates a new AlphaVantageAPI object with the stock symbol.
     *
     * @return new AplhaVantageAPI object
     */
    AlphaVantageAPI build() {
      return new AlphaVantageAPI(this.stockSymbol);
    }
  }

  @Override
  public String getStatus() {
    return status;
  }

  @Override
  public AlphaVantageAPI createURL() {
    try {
      url = new URL(alphaVantageAPI + stockSymbol + "&apikey=" + apiKey);
    } catch (MalformedURLException e) {
      status = "URL is broken";
      return null;
    }
    return this;
  }

  @Override
  public boolean works() {

    String apiHitLimitError = "{\n"
        + "    \"Note\": \"Thank you for using Alpha Vantage! Our standard API call frequency is "
        + "5 calls per minute and 500 calls per day. Please visit "
        + "https://www.alphavantage.co/premium/"
        + " if you would like to target a higher API call frequency.\"\n"
        + "}";

    String urlError = "{\n"
        + "    \"Error Message\": \"Invalid API call. Please retry or visit the documentation "
        + "(https://www.alphavantage.co/documentation/) for TIME_SERIES_DAILY.\"\n"
        + "}";

    data = fetch(url);
    if (data == null) {
      status = "URL is broken";
      return false;
    }

    if (data.toString().equals(apiHitLimitError)) {
      status = "api limit reached";
      return false;
    }
    if (data.toString().equals(urlError)) {
      status = "invalid ticker";
      return false;
    }
    return true;
  }

  @Override
  public void writeJson() {
    String replacedString = data.toString();
    replacedString = replacedString.replace("1. open", "open");
    replacedString = replacedString.replace("2. high", "high");
    replacedString = replacedString.replace("3. low", "low");
    replacedString = replacedString.replace("4. close", "close");
    replacedString = replacedString.replace("5. volume", "volume");

    JsonObject alphaDataJson = new Gson().fromJson(replacedString, JsonObject.class);
    JsonObject stockData = (JsonObject) alphaDataJson.get("Time Series (Daily)");

    try {
      new JSONFileOps().writeToFile(this.stockSymbol + "Data.json", "StocksJsonData",
          stockData.toString());
    } catch (IOException e) {
      status = "couldn't make static data dir";
    }

  }

  @Override
  public StringBuilder fetch(URL url) {
    return ApiHandler.super.fetch(url);
  }

}
