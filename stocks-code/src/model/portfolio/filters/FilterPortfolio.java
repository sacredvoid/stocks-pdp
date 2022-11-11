package model.portfolio.filters;

import java.util.HashMap;
import java.util.Map;
import model.portfolio.PortfolioData;

public class FilterPortfolio {

  public static Map<String, PortfolioData> getPortfolioAfterDate(
      Map<String, PortfolioData> parsedPFData,
      String date) {
    DatePredicate datePredicate = new DatePredicate(date);
    Map<String, PortfolioData> filteredPFData = new HashMap<>();
    parsedPFData.entrySet()
        .stream()
        .filter(entry -> datePredicate.test(entry.getKey())
        )
        .forEach(entry -> filteredPFData.put(entry.getKey(), entry.getValue()));
    return filteredPFData;
  }
}
