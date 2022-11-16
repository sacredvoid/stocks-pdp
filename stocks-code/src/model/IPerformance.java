package model;

import java.util.Map;
import model.portfolio.PortfolioData;

/**
 * Performance Interface defines the method signatures to build charts for different timespans.
 */
public interface IPerformance {

  /**
   * showPerformanceByDate is used to create a barchart with timespan for only 30 days.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and every day of the timespan provided
   */
  String showPerformanceByDate(Map<String, PortfolioData> pfData, String startDate, String endDate);

  /**
   * showPerformanceByMonth is used to create a barchart with timespan for only 30 months.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and every month of the timespan provided
   */
  String showPerformanceByMonth(Map<String, PortfolioData> pfData, String startDate,
      String endDate);

  /**
   * showPerformanceByQuarter is used to create a barchart with timespan for only 30 Quarters.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and quarter of the timespan provided
   */
  String showPerformanceByQuarter(Map<String, PortfolioData> pfData, String startDate,
      String endDate);

  /**
   * showPerformanceByHalfYear is used to create a barchart with timespan for only 30 days.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and every half year of the timespan provided
   */
  String showPerformanceByHalfYear(Map<String, PortfolioData> pfData, String startDate,
      String endDate);

  /**
   * showPerformanceByYear is used to create a barchart with timespan for only 30 days.
   *
   * @param pfData    PortfolioData which consists all the portfolio compositions
   * @param startDate Start Date of the timespan
   * @param endDate   End Date of the timespan
   * @return a Performance bar chart for each and every year of the timespan provided
   */
  String showPerformanceByYear(Map<String, PortfolioData> pfData, String startDate, String endDate);
}
