package model.apidata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * ApiDataAdapter class defines methods for reading and writing ApiData json objects.
 */
public class ApiDataAdapter {

  private static final Type typeToken = new TypeToken<LinkedHashMap<String, ApiDataStruct>>() {
  }.getType();

  /**
   * getApiObject() returns the ApiStockData as a dictionary of all the records from the API
   * source.
   *
   * @param jsonData from the API source
   * @return a Map of dates and ApiDataStruct objects which consists all the stock information for
   *     that date
   */
  public static Map<String, ApiDataStruct> getApiObject(String jsonData) {
    return new Gson().fromJson(jsonData, typeToken);
  }

}
