package model.performance;

import java.util.Map;
import java.util.TreeMap;
import model.portfolio.PortfolioData;

/**
 * Performance Interface defines the method signatures to build charts for different timespans.
 */
public interface IPerformance<T extends PortfolioData> {

  /**
   * showPerformanceByDate is used to create a barchart with timespan for only 30 days.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and every day of the timespan provided
   */
  TreeMap<String, Float> showPerformanceByDate(Map<String, T> pfData, String startDate,
      String endDate);

  /**
   * showPerformanceByMonth is used to create a barchart with timespan for only 30 months.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and every month of the timespan provided
   */
  TreeMap<String, Float> showPerformanceByMonth(Map<String, T> pfData, String startDate,
      String endDate);

  /**
   * showPerformanceByQuarter is used to create a barchart with timespan for only 30 Quarters.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and quarter of the timespan provided
   */
  TreeMap<String, Float> showPerformanceByQuarter(Map<String, T> pfData, String startDate,
      String endDate);

  /**
   * showPerformanceByHalfYear is used to create a barchart with timespan for only 30 days.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and every half year of the timespan provided
   */
  TreeMap<String, Float> showPerformanceByHalfYear(Map<String, T> pfData, String startDate,
      String endDate);

  /**
   * showPerformanceByYear is used to create a barchart with timespan for only 30 days.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and every year of the timespan provided
   */
  TreeMap<String, Float> showPerformanceByYear(Map<String, T> pfData, String startDate,
      String endDate);
}
