package model.portfolio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * PortfolioAdapter class defines the methods required to perform read and write operations for
 * PortfolioData.
 */
public class PortfolioDataAdapter {

  private static Type typeToken = new TypeToken<HashMap<String, PortfolioData>>() {
  }.getType();

  /**
   * getObject() method is used to read the Portfolio JSON Data and return it as a dictionary of
   * dates and PortfolioData objects
   *
   * @param jsonData actual portfolio json data as String
   * @return Map of date strings as keys and PortfolioData objects mapped to the respective date
   * keys
   */
  public static Map<String, PortfolioData> getObject(String jsonData) {
    return new Gson().fromJson(jsonData, typeToken);
  }

  /**
   * getJSON() method is used to read portfolio Json data as a dictionary of dates and PortfolioData
   * objects and return as a string.
   *
   * @param data the whole pora Map of date Strings and PortfolioData objects mapped to the
   *             respective date keys
   * @return the whole portfolio data as a string
   */
  public static String getJSON(Map<String, PortfolioData> data) {
    return new Gson().toJson(data);
  }

}
