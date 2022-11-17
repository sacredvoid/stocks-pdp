package model.portfolio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Utility class defines methods for additional operation that can be used on PortfolioData.
 */
public class Utility {

  /**
   * getLatestDate() method returns back the most recent date from a portfolio.
   *
   * @param pfData PortfolioData object
   * @return the most recent date key
   */
  public static String getLatestDate(Map<String, PortfolioData> pfData) {
    ArrayList<String> sortedDates = new ArrayList<>(pfData.keySet());
    if (sortedDates.isEmpty()) {
      return "No data found to sort";
    }
    sortedDates.sort(Collections.reverseOrder());
    return sortedDates.get(0);
  }

  /**
   * getOldestDate() method returns back the oldest date from a portfolio.
   *
   * @param pfData PortfolioData object
   * @return the oldest date key
   */
  public static String getOldestDate(Map<String, PortfolioData> pfData) {
    ArrayList<String> sortedDates = new ArrayList<>(pfData.keySet());
    if (sortedDates.isEmpty()) {
      return "No data found to sort";
    }
    Collections.sort(sortedDates);
    return sortedDates.get(0);
  }
}
