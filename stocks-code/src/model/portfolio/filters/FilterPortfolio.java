package model.portfolio.filters;

import java.util.HashMap;
import java.util.Map;
import model.portfolio.PortfolioData;

/**
 * The Filter portfolio class which helps get a sub-hashmap of portfolio data that exists
 * before/after a given date.
 */
public class FilterPortfolio {

  /**
   * Gets portfolio after date.
   *
   * @param parsedPFData the parsed pf data
   * @param date         the date
   * @return the portfolio after date
   */
  public static Map<String, PortfolioData> getPortfolioAfterDate(
      Map<String, PortfolioData> parsedPFData,
      String date) {
    DateAfterPredicate dateAfterPredicate = new DateAfterPredicate(date);
    Map<String, PortfolioData> filteredPFData = new HashMap<>();
    parsedPFData.entrySet()
        .stream()
        .filter(entry -> dateAfterPredicate.test(entry.getKey())
        )
        .forEach(entry -> filteredPFData.put(entry.getKey(), entry.getValue()));
    return filteredPFData;
  }

  /**
   * Gets portfolio before date.
   *
   * @param parsedPFData the parsed pf data
   * @param date         the date
   * @return the portfolio before date
   */
  public static Map<String, PortfolioData> getPortfolioBeforeDate(
      Map<String, PortfolioData> parsedPFData,
      String date) {
    DateBeforePredicate dateBeforePredicate = new DateBeforePredicate(date);
    Map<String, PortfolioData> filteredPFData = new HashMap<>();
    parsedPFData.entrySet()
        .stream()
        .filter(entry -> dateBeforePredicate.test(entry.getKey())
        )
        .forEach(entry -> filteredPFData.put(entry.getKey(), entry.getValue()));
    return filteredPFData;
  }

}
