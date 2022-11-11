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
import java.util.HashMap;
import model.RequestHandler.RequestHandlerBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class AlphaVantageAPI implements ApiHandler{
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


  static AlphaVantageApiBuilder getBuilder() {
    return new AlphaVantageApiBuilder();
  }

  static class AlphaVantageApiBuilder {

    private String stockSymbol;

    AlphaVantageApiBuilder stockSymbol(String stockSymbol) {
      this.stockSymbol = stockSymbol;
      return this;
    }


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
//    String [] errorMsgs = {"{\n"
//        + "    \"Note\": \"Thank you for using Alpha Vantage! Our standard API call frequency is "
//        + "5 calls per minute and 500 calls per day. Please visit "
//        + "https://www.alphavantage.co/premium/"
//        + " if you would like to target a higher API call frequency.\"\n"
//        + "}","{\n"
//        + "    \"Error Message\": \"Invalid API call. Please retry or visit the documentation "
//        + "(https://www.alphavantage.co/documentation/) for TIME_SERIES_DAILY.\"\n"
//        + "}"};
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
    if(data == null){
      status = "URL is broken";
      return false;
    }
//    for (String errorMsg: errorMsgs
//    ) {
//      if(data.toString().equals(errorMsg)){
//        return false;
//      }
//    }
    if(data.toString().equals(apiHitLimitError)){
      status = "api limit reached";
      return false;
    }
    if(data.toString().equals(urlError)){
      status = "invalid ticker";
      return false;
    }
    return true;
  }

  @Override
  public void writeJson() {
    String replacedString = data.toString();
    replacedString = replacedString.replace("1. open","open");
    replacedString = replacedString.replace("2. high","high");
    replacedString = replacedString.replace("3. low","low");
    replacedString = replacedString.replace("4. close","close");
    replacedString = replacedString.replace("5. volume","volume");

    JsonObject alphaDataJson = new Gson().fromJson(replacedString,JsonObject.class);
    JsonObject stockData = (JsonObject) alphaDataJson.get("Time Series (Daily)");

    Path path = Paths.get("app_data/StocksJsonData/"+stockSymbol+"Data.json");
    try(Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(stockData,writer);
    } catch (IOException e) {
      //
    }
//    return stockData;
  }

  @Override
  public StringBuilder fetch(URL url) {
    return ApiHandler.super.fetch(url);
  }

  public static void main(String args[]){
    String [] stockNames = {"AAPL","IBM","MSFT","GOOG","T","META","NFLX","NKE","ORCL","PEP","TSCO.LON","UNH","VZ","WMT","XOM","NVDA","TXN","JNJ","GPV.TRV"};
    for (String stock: stockNames
    ) {
      ApiHandler alphaApi1 = AlphaVantageAPI.getBuilder().stockSymbol(stock).build();
      if(alphaApi1.createURL().works()) {
        alphaApi1.writeJson();
      } else{
        System.out.println(stock+" data cannot be fetched");
      }
    }





//
//    ApiHandler alphaApi = AlphaVantageAPI.getBuilder().stockSymbol("adsfsf").build().createURL();
//    if(!alphaApi.works()){
//      System.out.println("condition working fine");
//    }
  }
}
