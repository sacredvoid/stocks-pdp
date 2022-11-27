package model.performance;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import model.apistockops.PortfolioValue;
import model.portfolio.PortfolioData;
import model.portfolio.PortfolioToCSVAdapter;
import model.validation.DateValidator;

/**
 * Performance is responsible for creating and displaying performance graph of the portfolio<p></p>
 * based on the date ranges.
 */
public class Performance implements IPerformance {

  private String pfId;

  private Performance(String pfId) {
    this.pfId = pfId;
  }

  /**
   * getBuilder() is a static method which returns a new PerfromanceBuilder object for <p></p>
   * building the Performance object.
   *
   * @return PerformanceBuilder
   */
  public static PerformanceBuilder getBuilder() {
    return new PerformanceBuilder();
  }

  /**
   * PerformanceBuilder is a static class that is used to build the Performance Object.
   */
  public static class PerformanceBuilder {

    private String pfId;

    /**
     * portfolioId() is a setter method for the portfolioId.
     *
     * @param pfId portfolio Id
     * @return the same PerformanceBuilder object after setting portfolioId
     */
    public PerformanceBuilder portfolioId(String pfId) {
      this.pfId = pfId;
      return this;
    }

    /**
     * build() method creates and returns a new Performance Object with portfolioId.
     *
     * @return Performance object
     */
    public Performance build() {
      return new Performance(this.pfId);
    }
  }

  @Override
  public String showPerformanceByDate(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {

    List<String> dates = new ArrayList<>();
    TreeMap<String, Float> dateValueDict = new TreeMap<String, Float>();

    dates = getDailyIntervals(startDate, endDate);
    // storing the previous stockQtyList
    String prevStockCountList = null;
    String stockCountList;
    for (String date : dates
    ) {
      if (!new DateValidator().checkData(date)) {
        continue;
      }

      TreeMap<String, PortfolioData> sortedSubMapByDate = fetchSubMap(pfData, date);

      if (sortedSubMapByDate.size() != 0) {
        PortfolioData lastEntry = sortedSubMapByDate.get(sortedSubMapByDate.lastKey());
        stockCountList = PortfolioToCSVAdapter.buildStockQuantityList(lastEntry.getStockList());
        prevStockCountList = stockCountList;

      } else {
        if (date.equals(startDate) || prevStockCountList == null) {
          dateValueDict.put(date, 0.0F);
          continue;
        }

        stockCountList = prevStockCountList;
      }

      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList, date);

      dateValueDict.put(date, portfolioValue);
    }

    return printGraph(dateValueDict);

  }

  @Override
  public String showPerformanceByMonth(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {
    List<String> yearAndMonths;
    TreeMap<String, Float> yearAndMonthValueDict = new TreeMap<String, Float>();
    String startYearAndMonth = startDate.substring(0, 7);
    String endYearAndMonth = endDate.substring(0, 7);

    yearAndMonths = getMonthlyIntervals(1, startDate, endDate, "add");

    String prevStockCountList = null;
    String stockCountList = null;
    for (String yearAndMonth : yearAndMonths
    ) {

      TreeMap<String, PortfolioData> sortedSubMapByYearAndMonth = fetchSubMap(pfData,
          yearAndMonth);

      if (sortedSubMapByYearAndMonth.size() != 0) {

        stockCountList = getLastEntryStockList(sortedSubMapByYearAndMonth);
        prevStockCountList = stockCountList;
      } else {
        if (yearAndMonth.equals(startYearAndMonth) || prevStockCountList == null) {
          yearAndMonthValueDict.put(yearAndMonth, 0.0F);
          continue;
        } else {
          stockCountList = prevStockCountList;

        }
      }
      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList,
          lastWorkingDay(yearAndMonth + "-28"));

      yearAndMonthValueDict.put(yearAndMonth, portfolioValue);
    }

    return printGraph(yearAndMonthValueDict);
  }

  @Override
  public String showPerformanceByQuarter(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {
    List<String> yearAndMonths;
    TreeMap<String, Float> yearAndMonthValueDict = new TreeMap<String, Float>();
    String startYearAndMonth = startDate.substring(0, 7);
    String endYearAndMonth = endDate.substring(0, 7);

    yearAndMonths = getMonthlyIntervals(3, startDate, endDate, "add");

    String prevStockCountList = null;
    String stockCountList = null;
    for (String yearAndMonth : yearAndMonths
    ) {
      TreeMap<String, PortfolioData> sortedSubMapByYearAndMonth = fetchSubMap(pfData,
          yearAndMonth);

      if (sortedSubMapByYearAndMonth.size() != 0) {

        stockCountList = getLastEntryStockList(sortedSubMapByYearAndMonth);
        prevStockCountList = stockCountList;

      } else {
        if (yearAndMonth.equals(startYearAndMonth)) {
          yearAndMonthValueDict.put(yearAndMonth, 0.0F);
          continue;
        }

        List<String> subYearAndMonths = getMonthlyIntervals(1, yearAndMonth
                + "-28",
            yearAndMonthValueDict.lastKey() + "-28", "sub");
        boolean flag = false;
        for (String subYearAndMonth : subYearAndMonths
        ) {
          TreeMap<String, PortfolioData> mostRecentSortedSubMap = fetchSubMap(pfData,
              subYearAndMonth);

          if (mostRecentSortedSubMap.size() != 0) {
            stockCountList = getLastEntryStockList(mostRecentSortedSubMap);
            prevStockCountList = stockCountList;
            flag = true;
            break;
          }
        }

        if (!flag) {
          if (prevStockCountList != null) {
            stockCountList = prevStockCountList;
          } else {
            yearAndMonthValueDict.put(yearAndMonth, 0.0F);
            continue;
          }

        }
      }
      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList,
          lastWorkingDay(yearAndMonth + "-28"));

      yearAndMonthValueDict.put(yearAndMonth, portfolioValue);
    }

    return printGraph(yearAndMonthValueDict);
  }

  @Override
  public String showPerformanceByHalfYear(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {
    List<String> yearAndMonths;
    TreeMap<String, Float> yearAndMonthValueDict = new TreeMap<String, Float>();
    String startYearAndMonth = startDate.substring(0, 7);
    String endYearAndMonth = endDate.substring(0, 7);

    yearAndMonths = getMonthlyIntervals(6, startDate, endDate, "add");

    String prevStockCountList = null;
    String stockCountList = null;
    for (String yearAndMonth : yearAndMonths
    ) {

      TreeMap<String, PortfolioData> sortedSubMapByYearAndMonth = fetchSubMap(pfData,
          yearAndMonth);

      if (sortedSubMapByYearAndMonth.size() != 0) {

        stockCountList = getLastEntryStockList(sortedSubMapByYearAndMonth);
        prevStockCountList = stockCountList;

      } else {
        if (yearAndMonth.equals(startYearAndMonth)) {
          yearAndMonthValueDict.put(yearAndMonth, 0.0F);
          continue;
        }
        List<String> subYearAndMonths = getMonthlyIntervals(1, yearAndMonth +
                "-28",
            yearAndMonthValueDict.lastKey() + "-28", "sub");
        boolean flag = false;
        for (String subYearAndMonth : subYearAndMonths
        ) {
          TreeMap<String, PortfolioData> mostRecentSortedSubMap = fetchSubMap(pfData,
              subYearAndMonth);

          if (mostRecentSortedSubMap.size() != 0) {
            stockCountList = getLastEntryStockList(mostRecentSortedSubMap);
            prevStockCountList = stockCountList;
            flag = true;
            break;
          }
        }

        if (!flag) {
          if (prevStockCountList != null) {
            stockCountList = prevStockCountList;
          } else {
            yearAndMonthValueDict.put(yearAndMonth, 0.0F);
            continue;
          }

        }
      }

      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList,
          lastWorkingDay(yearAndMonth + "-28"));

      yearAndMonthValueDict.put(yearAndMonth, portfolioValue);
    }

    return printGraph(yearAndMonthValueDict);
  }

  @Override
  public String showPerformanceByYear(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {
    String startYear = startDate.substring(0, 4);
    String endYear = endDate.substring(0, 4);

    List<String> years = new ArrayList<>();
    TreeMap<String, Float> yearValueDict = new TreeMap<>();
    String tempYear = startYear;
    while (!tempYear.equals(endYear)) {
      years.add(tempYear);
      tempYear = String.valueOf(Integer.parseInt(tempYear) + 1);
    }
    years.add(endYear);

    String prevStockCountList = null;
    String stockCountList;
    for (String year : years
    ) {

      TreeMap<String, PortfolioData> sortedSubMapByYear = fetchSubMap(pfData, year);

      if (sortedSubMapByYear.size() != 0) {

        stockCountList = getLastEntryStockList(sortedSubMapByYear);

        prevStockCountList = stockCountList;

      } else {
        if (year.equals(startYear) || prevStockCountList == null) {
          yearValueDict.put(year, 0.0F);
          continue;
        }
        stockCountList = prevStockCountList;
      }

      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList,
          lastWorkingDay(year + "-12-31"));

      yearValueDict.put(year, portfolioValue);
    }
    return printGraph(yearValueDict);
  }

  private String getLastEntryStockList(TreeMap<String, PortfolioData> subMap) {
    PortfolioData lastEntry = subMap.get(subMap.lastKey());
    return PortfolioToCSVAdapter.buildStockQuantityList(lastEntry.getStockList());
  }

  private TreeMap<String, PortfolioData> fetchSubMap(Map<String, PortfolioData> pfData,
      String timeSnap) {
    return pfData.entrySet().stream()
        .filter(x -> x.getKey().contains(timeSnap))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (oldValue, newValue) -> newValue, TreeMap::new));
  }

  private float fetchPortfolioValueForPerformance(String stockCountList, String date) {
    return PortfolioValue.getBuilder()
        .stockCountList(stockCountList)
        .date(date)
        .build()
        .stockCountValueForPerformance();
  }

  private String lastWorkingDay(String date) {
    LocalDate localDate;
    LocalDate lwd;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    localDate = LocalDate.parse(date, formatter).with(TemporalAdjusters.lastDayOfMonth());
    switch (DayOfWeek.of(localDate.get(ChronoField.DAY_OF_WEEK))) {
      case SATURDAY:
        lwd = localDate.minusDays(1);
        break;
      case SUNDAY:
        lwd = localDate.minusDays(2);
        break;
      default:
        lwd = localDate;
    }
    return lwd.format(formatter);

  }

  private List<String> getMonthlyIntervals(int monthInterval, String startDate, String endDate,
      String choice) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate lStartDate = LocalDate.parse(startDate, df);
    LocalDate lEndDate = LocalDate.parse(endDate, df);
    List<String> allyearAndMonths = new ArrayList<String>();
    LocalDate lTempYearAndMonth = lStartDate;
    if (choice.equals("add")) {
      while (lTempYearAndMonth.isBefore(lEndDate)) {
        allyearAndMonths.add(lTempYearAndMonth.format(df).substring(0, 7));
        lTempYearAndMonth = lTempYearAndMonth.plusMonths(monthInterval);
      }
      allyearAndMonths.add(lEndDate.format(df).substring(0, 7));
    } else if (choice.equals("sub")) {
      while (lTempYearAndMonth.isAfter(lEndDate)) {
        allyearAndMonths.add(lTempYearAndMonth.format(df).substring(0, 7));
        lTempYearAndMonth = lTempYearAndMonth.minusMonths(monthInterval);
      }
    }
    return allyearAndMonths;
  }

  private List<String> getDailyIntervals(String startDate, String endDate) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate lStartDate = LocalDate.parse(startDate, df);
    LocalDate lEndDate = LocalDate.parse(endDate, df);
    List<String> allDates = new ArrayList<String>();
    LocalDate lTempDate = lStartDate;

    while (lTempDate.isBefore(lEndDate)) {
      allDates.add(lTempDate.format(df));
      lTempDate = lTempDate.plusDays(1);
    }
    allDates.add(lEndDate.format(df));
    return allDates;
  }

  private String printGraph(TreeMap<String, Float> sequenceData) {
    String graph = "";
    String startTimeSpan = sequenceData.firstKey();
    String endTimeSpan = sequenceData.lastKey();
    int scale = ((int) (Collections.max(sequenceData.values()) / 50 + 99) / 100) * 100;

    graph +=
        "Performance of portfolio " + this.pfId + " from " + startTimeSpan + " to " + endTimeSpan
            + "\n\n";
    for (Entry<String, Float> entry : sequenceData.entrySet()
    ) {
      graph += entry.getKey() + ": " + printStars(entry.getValue(), scale) + "\n";
    }

    graph += "\nScale: * = $" + String.valueOf(scale);

    return graph;
  }

  private String printStars(float value, int scale) {
    int noOfStars = ((int) value / (int) scale);
    String stars = "";
    for (int i = 0; i < noOfStars; i++) {
      stars += "*";
    }
    return stars;
  }

}
