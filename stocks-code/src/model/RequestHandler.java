package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
//import model.apiData.ApiDataList;
//import model.apiData.ApiDataMapper;
//import org.json.JSONObject;
//import org.json.JSONArray;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import model.apiData.ApiDataStruct;
import model.fileops.CSVFileOps;


/**
 * RequestHandler class is responsible for creating requests and fetching the data from the<p></p>
 * the Alphavantage API.
 */
class RequestHandler {

  private String stockSymbol;
  private ApiHandler apiModel ;

  private Map<String, ApiDataStruct> data;


  private String status = "success";
  private boolean urlFlag = true;
  private RequestHandler(String stockSymbol) {
    this.stockSymbol = stockSymbol;
  }

  /**
   * getBuilder() is a static method which returns a new RequestHandlerBuilder object for<p></p>
   * building the RequestHandler object.
   *
   * @return an RequestHandlerBuilder object
   */
  static RequestHandlerBuilder getBuilder() {
    return new RequestHandlerBuilder();
  }

  /**
   * RequestHandlerBuilder class is a static class which is used to build the object of<p></p>
   * RequestHandler class.
   */
  static class RequestHandlerBuilder {

    private String stockSymbol;

    /**
     * the stockSymbol method is a setter method for the stockSymbol to the<p></p>
     * RequestHandlerBuilder class.
     *
     * @param stockSymbol name or symbol of the stock
     * @return the same RequestHandlerBuilder object with the stock symbol stored as an attribute.
     */
    RequestHandlerBuilder stockSymbol(String stockSymbol) {
      this.stockSymbol = stockSymbol;
      return this;
    }

    /**
     * build() method of the RequestHandlerBuilder class creates a new object of the <p></p>
     * RequestHandler object with the stock symbol.
     *
     * @return new object of the RequestHandler class
     */
    RequestHandler build() {
      return new RequestHandler(this.stockSymbol);
    }
  }
  public String getStatus() {
    return status;
  }

  /**
   * buildURL() method creates the URL for the API request using the name and symbol <p></p> of the
   * stock passed to it.
   *
   * @return the same RequestHandler object with url built and stored as an attribute
   */
  public RequestHandler buildURL() {

    apiModel = AlphaVantageAPI.getBuilder().stockSymbol(stockSymbol).build();
    if(apiModel instanceof AlphaVantageAPI){
      if(apiModel.createURL() == null || apiModel.createURL().works()==false){
//        apiModel = (YahooStockAPI)apiModel;
        urlFlag = false;
      }
      else{
        apiModel.writeJson();
      }
      status = apiModel.getStatus();
    }

//    if(apiModel instanceof YahooStockAPI){
//      if(apiModel.createURL() == null || !apiModel.createURL().works()){
//        return null;
//      }
//      else{
//        apiModel.writeJson();
//      }
//    }
    return this;
  }

  /**
   * fetch() method fetches the data from the API using the URL.
   *
   * @return Stock data in the form of String
   */

  Map<String, ApiDataStruct> fetch() {
    if(!urlFlag){
      return null;
    }
    Type token = new TypeToken<LinkedHashMap<String,ApiDataStruct>>(){}.getType();
    try {
      data = new Gson().fromJson(new FileReader("app_data/StocksJsonData/"+stockSymbol+"Data.json"),
          token);
    } catch (FileNotFoundException e) {
//      throw new RuntimeException(e);
      status ="no data found";
      return null;
    }
    return data;
  }

  public static void main(String args[]){
    Map<String, ApiDataStruct> dataList ;
    dataList = RequestHandler.getBuilder().stockSymbol("AAPL").build().buildURL().fetch();
    for (Entry<String, ApiDataStruct> entry: dataList.entrySet()
    ) {
      System.out.println(entry.getKey());
      System.out.println(entry.getValue().getOpen());
    }
  }
}
