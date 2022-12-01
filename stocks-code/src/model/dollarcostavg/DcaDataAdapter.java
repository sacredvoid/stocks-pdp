package model.dollarcostavg;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import model.portfolio.DollarCostAveragePortfolio;

public class DcaDataAdapter {
  private static final Type typeToken = new TypeToken<HashMap<String, DollarCostAveragePortfolio>>() {
  }.getType();


  public static Map<String, DollarCostAveragePortfolio> getDcaPortfolioObject(String jsonData) {
    return new Gson().fromJson(jsonData, typeToken);
  }

}
