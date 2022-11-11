package model.portfolio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PortfolioDataAdapter {

  private static Type typeToken = new TypeToken<HashMap<String,PortfolioData>>() {}.getType();

  public static Map<String, PortfolioData> getObject(String jsonData) {
    return new Gson().fromJson(jsonData,typeToken);
  }

  public static String getJSON(Map<String, PortfolioData> data) {
    return new Gson().toJson(data);
  }

}
