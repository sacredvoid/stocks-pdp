package model.portfolio.filters;

import java.util.HashMap;
import java.util.Map;
import model.portfolio.PortfolioData;

public class FilterPortfolio {

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
