package model;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import model.fileops.JSONFileOps;
import model.portfolio.PortfolioData;
import model.portfolio.PortfolioDataAdapter;
import model.portfolio.PortfolioToCSVAdapter;
import model.validation.DateValidator;

public class Performance implements IPerformance{

  private String pfId;

  private Performance(String pfId){
    this.pfId = pfId;
  }

  public static PerformanceBuilder getBuilder(){
    return new PerformanceBuilder();
  }

  public static class PerformanceBuilder{
    private String pfId;

    public PerformanceBuilder portfolioId(String pfId){
      this.pfId = pfId;
      return this;
    }

    public Performance build(){
      return new Performance(this.pfId);
    }
  }
  @Override
  public String showPerformanceByDate(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {

    List<String> dates = new ArrayList<>();
    TreeMap<String,Float> dateValueDict = new TreeMap<String,Float>(); // Map to store the year and

    dates = getDailyIntervals(startDate,endDate);
    // storing the previous stockQtyList
    String prevStockCountList = null;
    String stockCountList;
    for (String date: dates
    ) {
      try{
        if(!new DateValidator().checkData(date)){
          continue;
        }
      }catch (ParseException e){
        //
      }
      //fetching all the submaps of one particular year and ordering them
      TreeMap<String,PortfolioData> sortedSubMapByYear = fetchSubMap(pfData,date);

      if(sortedSubMapByYear.size()!=0){
        //getting the last submap's stockQty composition
        PortfolioData lastEntry = sortedSubMapByYear.get(sortedSubMapByYear.lastKey());

        //fetching the stockQtylist
        stockCountList = PortfolioToCSVAdapter.buildStockQuantityList(lastEntry.getStockList());

        //storing the stockQtyList, so that if there's no record of a specific year in the portfolio
        // the previous years composition is used
        prevStockCountList = stockCountList;
//        System.out.printf(String.format("%s %f",year,portfolioValue));
      }
      else{
        if(date.equals(startDate) || prevStockCountList==null){
          dateValueDict.put(date,0.0F);
          continue;
        }
        //the prevStockCountlist is used as the stockCountList for the year with no records in portfoli
        stockCountList = prevStockCountList;
      }

      // fetching the portfolioValue
      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList,date);

      dateValueDict.put(date,portfolioValue); //the year and value stored in the map and passed to the graphPrinterFunction
    }



    return printGraph(dateValueDict);
//    return null;
  }

  @Override
  public String showPerformanceByMonth(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {
    List<String> yearAndMonths;
    TreeMap<String,Float> yearAndMonthValueDict = new TreeMap<String,Float>();
    String startYearAndMonth = startDate.substring(0,7);
    String endYearAndMonth = endDate.substring(0,7);

    yearAndMonths = getMonthlyIntervals(1,startDate,endDate,"add");

    String prevStockCountList = null;
    String stockCountList = null;
    for (String yearAndMonth: yearAndMonths
    ) {
      //fetching all the submaps of one particular yearAndMonth and ordering them
      TreeMap<String,PortfolioData> sortedSubMapByYear = fetchSubMap(pfData,yearAndMonth);

      if(sortedSubMapByYear.size()!=0){
        //getting the last submap's stockQty composition
//        PortfolioData lastEntry = sortedSubMapByYear.get(sortedSubMapByYear.lastKey());

        //fetching the stockQtylist
        stockCountList = getLastEntryStockList(sortedSubMapByYear);

        //storing the stockQtyList, so that if there's no record of a specific year in the portfolio
        // the previous years composition is used
        prevStockCountList = stockCountList;
//        System.out.printf(String.format("%s %f",year,portfolioValue));
      }
      else{
        if(yearAndMonth.equals(startYearAndMonth) || prevStockCountList==null){
          yearAndMonthValueDict.put(yearAndMonth,0.0F);
          continue;
        }
        else{
          stockCountList = prevStockCountList;

        }
      }
      // fetching the portfolioValue
      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList,lastWorkingDay(yearAndMonth+"-28"));

      yearAndMonthValueDict.put(yearAndMonth,portfolioValue); //the year and value stored in the map and passed to the graphPrinterFunction
    }

    return printGraph(yearAndMonthValueDict);
  }

  @Override
  public String showPerformanceByQuarter(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {
    List<String> yearAndMonths;
    TreeMap<String,Float> yearAndMonthValueDict = new TreeMap<String,Float>();
    String startYearAndMonth = startDate.substring(0,7);
    String endYearAndMonth = endDate.substring(0,7);

    yearAndMonths = getMonthlyIntervals(3,startDate,endDate,"add");

    String prevStockCountList = null;
    String stockCountList = null;
    for (String yearAndMonth: yearAndMonths
    ) {
      //fetching all the submaps of one particular yearAndMonth and ordering them
      TreeMap<String,PortfolioData> sortedSubMapByYear = fetchSubMap(pfData,yearAndMonth);

      if(sortedSubMapByYear.size()!=0){
        //getting the last submap's stockQty composition
//        PortfolioData lastEntry = sortedSubMapByYear.get(sortedSubMapByYear.lastKey());

        //fetching the stockQtylist
        stockCountList = getLastEntryStockList(sortedSubMapByYear);

        //storing the stockQtyList, so that if there's no record of a specific year in the portfolio
        // the previous years composition is used
        prevStockCountList = stockCountList;
//        System.out.printf(String.format("%s %f",year,portfolioValue));
      }
      else{
        if(yearAndMonth.equals(startYearAndMonth)){
          yearAndMonthValueDict.put(yearAndMonth,0.0F);
          continue;
        }
        //the prevStockCountlist is used as the stockCountList for the year with no records in portfolio
        List<String> subYearAndMonths = getMonthlyIntervals(1,yearAndMonth+"-28", yearAndMonthValueDict.lastKey()+"-28","sub");
        boolean flag = false;
        for (String subYearAndMonth : subYearAndMonths
        ) {
          TreeMap<String,PortfolioData> mostRecentSortedSubMap = fetchSubMap(pfData,subYearAndMonth);

          if(mostRecentSortedSubMap.size()!=0){
//            PortfolioData mostRecentEntry = mostRecentSortedSubMap.get(mostRecentSortedSubMap.lastKey());
            stockCountList = getLastEntryStockList(mostRecentSortedSubMap);
            prevStockCountList = stockCountList;
            flag = true;
            break;
          }
        }

        if(flag == false){
          if(prevStockCountList != null){
            stockCountList = prevStockCountList;
          }
          else{
            yearAndMonthValueDict.put(yearAndMonth,0.0F);
            continue;
          }

        }
      }
//      prevStockCountList = stockCountList;

      // fetching the portfolioValue
      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList,lastWorkingDay(yearAndMonth+"-28"));

      yearAndMonthValueDict.put(yearAndMonth,portfolioValue); //the year and value stored in the map and passed to the graphPrinterFunction
    }

    return printGraph(yearAndMonthValueDict);
  }

  @Override
  public String showPerformanceByHalfYear(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {
    List<String> yearAndMonths;
    TreeMap<String,Float> yearAndMonthValueDict = new TreeMap<String,Float>();
    String startYearAndMonth = startDate.substring(0,7);
    String endYearAndMonth = endDate.substring(0,7);

    yearAndMonths = getMonthlyIntervals(6,startDate,endDate,"add");

    String prevStockCountList = null;
    String stockCountList = null;
    for (String yearAndMonth: yearAndMonths
    ) {
      //fetching all the submaps of one particular yearAndMonth and ordering them
      TreeMap<String,PortfolioData> sortedSubMapByYear = fetchSubMap(pfData,yearAndMonth);

      if(sortedSubMapByYear.size()!=0){
        //getting the last submap's stockQty composition
//        PortfolioData lastEntry = sortedSubMapByYear.get(sortedSubMapByYear.lastKey());

        //fetching the stockQtylist
        stockCountList = getLastEntryStockList(sortedSubMapByYear);

        //storing the stockQtyList, so that if there's no record of a specific year in the portfolio
        // the previous years composition is used
        prevStockCountList = stockCountList;
//        System.out.printf(String.format("%s %f",year,portfolioValue));
      }
      else{
        if(yearAndMonth.equals(startYearAndMonth)){
          yearAndMonthValueDict.put(yearAndMonth,0.0F);
          continue;
        }
        //the prevStockCountlist is used as the stockCountList for the year with no records in portfolio
        List<String> subYearAndMonths = getMonthlyIntervals(1,yearAndMonth+"-28", yearAndMonthValueDict.lastKey()+"-28","sub");
        boolean flag = false;
        for (String subYearAndMonth : subYearAndMonths
        ) {
          TreeMap<String,PortfolioData> mostRecentSortedSubMap = fetchSubMap(pfData,subYearAndMonth);

          if(mostRecentSortedSubMap.size()!=0){
//            PortfolioData mostRecentEntry = mostRecentSortedSubMap.get(mostRecentSortedSubMap.lastKey());
            stockCountList = getLastEntryStockList(mostRecentSortedSubMap);
            prevStockCountList = stockCountList;
            flag = true;
            break;
          }
        }

        if(flag == false){
          if(prevStockCountList != null){
            stockCountList = prevStockCountList;
          }
          else{
            yearAndMonthValueDict.put(yearAndMonth,0.0F);
            continue;
          }

        }
      }
//      prevStockCountList = stockCountList;

      // fetching the portfolioValue
      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList,lastWorkingDay(yearAndMonth+"-28"));

      yearAndMonthValueDict.put(yearAndMonth,portfolioValue); //the year and value stored in the map and passed to the graphPrinterFunction
    }

//    System.out.println(yearAndMonthValueDict);
    return printGraph(yearAndMonthValueDict);
  }

  @Override
  public String showPerformanceByYear(Map<String, PortfolioData> pfData, String startDate,
      String endDate) {
    //extracting years from the start and end dates
    String startYear = startDate.substring(0,4);
    String endYear = endDate.substring(0,4);

    List<String> years = new ArrayList<>();
    TreeMap<String,Float> yearValueDict = new TreeMap<String,Float>(); // Map to store the year and

    //generating all the years in the timespan
    String tempYear = startYear;
    while(!tempYear.equals(endYear)){
      years.add(tempYear);
      tempYear = String.valueOf(Integer.parseInt(tempYear)+1);
    }
    years.add(endYear);

    // storing the previous stockQtyList
    String prevStockCountList = null;
    String stockCountList;
    for (String year: years
    ) {
      //fetching all the submaps of one particular year and ordering them
      TreeMap<String,PortfolioData> sortedSubMapByYear = fetchSubMap(pfData, year);

      if(sortedSubMapByYear.size()!=0){
        //getting the last submap's stockQty composition
//        PortfolioData lastEntry = sortedSubMapByYear.get(sortedSubMapByYear.lastKey());

        //fetching the stockQtylist
        stockCountList = getLastEntryStockList(sortedSubMapByYear);

        //storing the stockQtyList, so that if there's no record of a specific year in the portfolio
        // the previous years composition is used
        prevStockCountList = stockCountList;
//        System.out.printf(String.format("%s %f",year,portfolioValue));
      }
      else{
        if(year.equals(startYear) || prevStockCountList==null){
          yearValueDict.put(year,0.0F);
          continue;
        }
        //the prevStockCountlist is used as the stockCountList for the year with no records in portfoli
        stockCountList = prevStockCountList;
      }

      // fetching the portfolioValue
      float portfolioValue = fetchPortfolioValueForPerformance(stockCountList,lastWorkingDay(year+"-12-31"));

      yearValueDict.put(year,portfolioValue); //the year and value stored in the map and passed to the graphPrinterFunction
    }


    return printGraph(yearValueDict);
  }

  private String getLastEntryStockList(TreeMap<String,PortfolioData> subMap){
    PortfolioData lastEntry = subMap.get(subMap.lastKey());
    return PortfolioToCSVAdapter.buildStockQuantityList(lastEntry.getStockList());
  }
  private TreeMap<String, PortfolioData> fetchSubMap(Map<String,PortfolioData> pfData,String timeSnap){
    return pfData.entrySet().stream()
        .filter(x -> x.getKey().contains(timeSnap))
        .collect(Collectors.toMap(Map.Entry::getKey , Map.Entry::getValue,(oldValue,newValue)->newValue,TreeMap::new));
  }

  private float fetchPortfolioValueForPerformance(String stockCountList, String date){
    return PortfolioValue.getBuilder()
        .stockCountList(stockCountList)
        .date(date)
        .build()
        .stockCountValueForPerformance();
  }
  private String lastWorkingDay(String date)  {
    LocalDate localDate;
    LocalDate lwd;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    localDate = LocalDate.parse(date,formatter).with(TemporalAdjusters.lastDayOfMonth());
    switch(DayOfWeek.of(localDate.get(ChronoField.DAY_OF_WEEK))){
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

  private List<String> getMonthlyIntervals(int monthInterval, String startDate, String endDate, String choice){
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate lStartDate = LocalDate.parse(startDate,df);
    LocalDate lEndDate = LocalDate.parse(endDate,df);
    List<String> allyearAndMonths = new ArrayList<String>();
    LocalDate lTempYearAndMonth = lStartDate;
    if(choice.equals("add")) {
      while (lTempYearAndMonth.isBefore(lEndDate)) {
        allyearAndMonths.add(lTempYearAndMonth.format(df).substring(0, 7));
        lTempYearAndMonth = lTempYearAndMonth.plusMonths(monthInterval);
      }
      allyearAndMonths.add(lEndDate.format(df).substring(0, 7));
    } else if(choice.equals("sub")){
      while(lTempYearAndMonth.isAfter(lEndDate)){
        allyearAndMonths.add(lTempYearAndMonth.format(df).substring(0, 7));
        lTempYearAndMonth = lTempYearAndMonth.minusMonths(monthInterval);
      }
    }
    return allyearAndMonths;
  }

  private List<String> getDailyIntervals(String startDate, String endDate){
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate lStartDate = LocalDate.parse(startDate,df);
    LocalDate lEndDate = LocalDate.parse(endDate,df);
    List<String> allDates = new ArrayList<String>();
    LocalDate lTempDate = lStartDate;

    while (lTempDate.isBefore(lEndDate)) {
      allDates.add(lTempDate.format(df));
      lTempDate = lTempDate.plusDays(1);
    }
    allDates.add(lEndDate.format(df));
    return allDates;
  }

  private String printGraph(TreeMap<String,Float> sequenceData){
    String graph = "";
    String startTimeSpan = sequenceData.firstKey();
    String endTimeSpan = sequenceData.lastKey();
    int scale = ((int)(Collections.max(sequenceData.values())/50 +99) /100) * 100;
//    float scale = Collections.min(sequenceData.values());
    // sequences / 50 <
    graph += "Performance of portfolio "+this.pfId+" from "+startTimeSpan+" to "+endTimeSpan+"\n\n";
    for (Entry<String,Float> entry: sequenceData.entrySet()
    ) {
      graph += entry.getKey()+": "+printStars(entry.getValue(),scale)+"\n";
    }

    graph +="\nScale: * = $"+String.valueOf(scale);
//    System.out.println(sequenceData);
    return graph;
  }

  private String printStars(float value, int scale){
    int noOfStars = ((int) value/(int)scale);
//    System.out.println(noOfStars);
    String stars = "";
    for(int i=0 ; i< noOfStars;i++){
      stars+="*";
    }
    return stars;
  }
//  public static void main(String ags[]) throws FileNotFoundException {
//    Performance pob = new Performance();
//    Map<String,PortfolioData> pfData = PortfolioDataAdapter.getObject(new JSONFileOps().readFile("893565.json","/PortfolioData/"));
//    System.out.println(pob.showPerformanceByYear(pfData,"2016-10-11","2020-10-11"));
//
//    Map<String,PortfolioData> pfData = PortfolioDataAdapter.getObject(new JSONFileOps().readFile("123455.json","/PortfolioData/"));
//    System.out.println(pob.showPerformanceByMonth(pfData,"2016-11-11","2022-11-11"));
//  }
}
