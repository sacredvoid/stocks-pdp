package model.portfolio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Utility {
  public static String getLatestDate(Map<String, PortfolioData> pfData) {
    ArrayList<String> sortedDates = new ArrayList<>(pfData.keySet());
    if(sortedDates.isEmpty()) {
      return "No data found to sort";
    }
    sortedDates.sort(Collections.reverseOrder());
    return sortedDates.get(0);
  }
}
