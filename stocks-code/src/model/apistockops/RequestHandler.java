package model.apistockops;

import java.io.FileNotFoundException;
import java.util.Map;
import model.apistockops.AlphaVantageAPI;
import model.apiData.ApiDataAdapter;
import model.apiData.ApiDataStruct;
import model.fileops.JSONFileOps;


/**
 * RequestHandler class is responsible for creating APIcalls handling multiple API data<p></p>
 * sources by interacting with the respective API Classes.
 */
public class RequestHandler {

  private String stockSymbol;
  private ApiHandler apiModel;

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

  /**
   * getStatus() method returns the status of the API request.
   *
   * @return the status message
   */
  public String getStatus() {
    return status;
  }

  /**
   * buildURL() method takes the stockSymbol and pass it to the respective API classes<p></p> and
   * builds the respective URL and saves it on local machine.
   *
   * @return the same RequestHandler object with url built and stored as an attribute
   */
  public RequestHandler buildURL() {

    apiModel = AlphaVantageAPI.getBuilder().stockSymbol(stockSymbol).build();
    if (apiModel instanceof AlphaVantageAPI) {
      if (apiModel.createURL() == null || apiModel.createURL().works() == false) {
        urlFlag = false;
      } else {
        apiModel.writeJson();
      }
      status = apiModel.getStatus();
    }
    return this;
  }

  /**
   * fetch() method reads the data that was saved on the local machine by the RequestHandler and
   * returns it back.
   *
   * @return Stock data in the form of Map Object
   */

  Map<String, ApiDataStruct> fetch() {
    if (!urlFlag) {
      return null;
    }

    try {
      data = ApiDataAdapter.getApiObject(
          new JSONFileOps().readFile(this.stockSymbol + "Data.json", "StocksJsonData"));
    } catch (FileNotFoundException e) {

      status = "no data found";
      return null;
    }
    return data;
  }
}
