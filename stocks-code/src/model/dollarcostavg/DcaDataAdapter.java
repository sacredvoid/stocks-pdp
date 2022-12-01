package model.dollarcostavg;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import model.portfolio.DollarCostAveragePortfolio;

/**
 * Adapter class to convert DCA Portfolio Object to it's JSON string and vice-versa.
 */
public class DcaDataAdapter {

  private static final Type typeToken = new TypeToken<HashMap<String, DollarCostAveragePortfolio>>() {
  }.getType();


  /**
   * Gets dca portfolio object given the json string.
   *
   * @param jsonData the json data
   * @return the dca portfolio object
   */
  public static Map<String, DollarCostAveragePortfolio> getDcaPortfolioObject(String jsonData) {
    return new Gson().fromJson(jsonData, typeToken);
  }

}
