package model.apiData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import model.portfolio.PortfolioData;

/**
 * ApiDataAdapter class defines methods for additional operations on API Stock Data.
 */
public class ApiDataAdapter {

  private static Type typeToken = new TypeToken<LinkedHashMap<String, ApiDataStruct>>() {
  }.getType();

  public static Map<String, ApiDataStruct> getApiObject(String jsonData) {
    return new Gson().fromJson(jsonData, typeToken);
  }

}
