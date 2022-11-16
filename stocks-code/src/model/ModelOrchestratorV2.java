package model;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.fileops.FileOps;
import model.fileops.JSONFileOps;
import model.portfolio.CSVToPortfolioAdapter;
import model.portfolio.PortfolioData;
import model.portfolio.PortfolioDataAdapter;
import model.portfolio.PortfolioToCSVAdapter;
import model.portfolio.StockData;
import model.portfolio.Utility;
import model.portfolio.filters.FilterPortfolio;


public class ModelOrchestratorV2 extends AOrchestrator {
  private static final String VALID_DATE_REGEX =
      "q|Q|(19|20)[0-9]{2}-[0-9]{2}-[0-9]{2}";
  private FileOps jsonParser = new JSONFileOps();

  @Override
  public String getLatestPortfolioComposition(String portfolioID) throws FileNotFoundException {
    String pfData = jsonParser.readFile(portfolioID + ".json", PORTFOLIO_DATA_PATH);
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);
    String latestDate = Utility.getLatestDate(parsedPFData);
    String csvPortfolio = PortfolioToCSVAdapter.buildStockQuantityList(
        parsedPFData.get(latestDate).getStockList()
    );
    return csvPortfolio;
  }

  @Override
  public String createPortfolio(String portfolioData) {
    if(portfolioData.equals("no data provided")){
      return "No data provided. No portfolio was created";
    }
    String newPFID = this.generatePortfolioID();
    try {
      Map<String, PortfolioData> translated = CSVToPortfolioAdapter.buildPortfolioData(
          portfolioData);
      jsonParser.writeToFile(newPFID + ".json", PORTFOLIO_DATA_PATH, new Gson().toJson(translated));
    } catch (IOException io) {
      return "Failed to create portfolio";
    }
//    catch (NumberFormatException e){
//      return "Failed to create portfolio.\nMake sure QUANTITY is in only Numbers for each input";
//    } catch (IllegalArgumentException e){
//      return e.getMessage();
//    }
    return "Created Portfolio with ID: " + newPFID;
  }

  @Override
  public String getPortfolioValue(String date, String pfId) throws ParseException {
    String stockCountList;
    try{
      stockCountList = this.getPortfolioCompositionByDate(date,pfId);
    }catch( FileNotFoundException e){
      return "Sorry, No stocks for given date"; // Get most recent stock list
    }
    if(stockCountList.contains("Sorry")){
      return stockCountList;
    }
    List<String> portfolioValue = PortfolioValue.getBuilder()
        .stockCountList(stockCountList)
        .date(date)
        .build()
        .completePortfolioValue();

    String portfolioValueCsvFormat = String.join("\n",portfolioValue);
//    System.out.println(portfolioValueCsvFormat);
//    return null;
    return portfolioValueCsvFormat;
  }

  @Override
  public String getPortfolioCompositionByDate(String date, String pfID) throws FileNotFoundException {
    String pfData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);

//    // Example for filtering data
    Map<String, PortfolioData> filteredData = FilterPortfolio.getPortfolioBeforeDate(parsedPFData,date);
    String latestDate = Utility.getLatestDate(filteredData);
    List<StockData> stockDataForDate;
    try {
      stockDataForDate = filteredData.getOrDefault(latestDate,null).getStockList();
    }
    catch (NullPointerException n) {
      return "Sorry, no portfolio data found for given date/before it.";
    }
    if(stockDataForDate != null) {
      String stockDataCSV = PortfolioToCSVAdapter.buildStockQuantityList(stockDataForDate);
//      System.out.println(stockDataCSV);
      return stockDataCSV;
    }
    else return "Sorry, no stocks for given date.";
    //    System.out.println();
  }

  @Override
  public String loadExternalPortfolio(String path) throws FileNotFoundException {
    return null;
  }

  /**
   * Shows the line chart performance of a specified portfolio over the timespan provided<p></p>
   * by the user.
   * @param pfId Portfolio id of the portfolio
   * @param startDate Starting date of the timespan
   * @param endDate Ending date of the timespan
   * @return performance of the portfolio for each timestamp in the form of stars which depict<p></p>
   *          the value of the portfolio
   */
//  public String showPerformance(String pfId, String startDate, String endDate) throws FileNotFoundException{
//    String pfData = jsonParser.readFile(pfId + ".json", PORTFOLIO_DATA_PATH);
//    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);
//
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    Period timespan = Period.between(LocalDate.parse(startDate,formatter),
//        LocalDate.parse(endDate,formatter));
//
//    if(timespan.getDays() >=5 && timespan.getDays() <=30){
//      return new Performance().showPerformanceByDate(parsedPFData,startDate,endDate);
//    }else if( timespan.getMonths() >= 5 && timespan.getMonths() <=30){
//      return new Performance().showPerformanceByMonth(parsedPFData,startDate,endDate);
//    } else if(timespan.getMonths() >= 15 && timespan.getMonths() <=90){
//      return new Performance().showPerformanceByQuarter(parsedPFData,startDate,endDate);
//    } else if( timespan.getMonths() >= 30 && timespan.getMonths()<=180){
//      return new Performance().showPerformanceByHalfYear(parsedPFData,startDate,endDate);
//    } else{
//      return new Performance().showPerformanceByYear(parsedPFData,startDate,endDate);
//    }

//    return null;
//  }
}
