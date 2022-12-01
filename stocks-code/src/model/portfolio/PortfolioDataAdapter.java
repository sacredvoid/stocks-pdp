package model.portfolio;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * PortfolioAdapter class defines the methods required to perform read and write operations for
 * PortfolioData.
 */
public class PortfolioDataAdapter<T extends PortfolioData> {
  /**
   * getObject() method is used to read the Portfolio JSON Data and return it as a dictionary of
   * dates and PortfolioData objects.
   *
   * @param   jsonData actual portfolio json data as String
   * @return  Map of date strings as keys and PortfolioData objects mapped to the respective date
   *          keys
   */
  public static <T extends PortfolioData> Map<String, T> getObject(String jsonData, Type generic) {
    return new Gson().fromJson(jsonData, generic);
  }

  /**
   * getJSON() method is used to read portfolio Json data as a dictionary of dates and PortfolioData
   * objects and return as a string.
   *
   * @param data the whole pora Map of date Strings and PortfolioData objects mapped to the
   *             respective date keys
   * @return the whole portfolio data as a string
   */
  public static <T extends PortfolioData> String getJSON(Map<String, T> data) {
    return new Gson().toJson(data);
  }

}
