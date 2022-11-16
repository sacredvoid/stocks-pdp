package model;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import model.fileops.FileOps;
import model.fileops.JSONFileOps;
import model.portfolio.CSVToPortfolioAdapter;
import model.portfolio.PortfolioData;
import model.portfolio.PortfolioDataAdapter;
import model.portfolio.PortfolioToCSVAdapter;
import model.portfolio.StockData;
import model.portfolio.Utility;
import model.portfolio.filters.FilterPortfolio;
import java.time.temporal.ChronoUnit;

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
    if (portfolioData.equals("no data provided")) {
      return "No data provided. No portfolio was created";
    }
    String newPFID = this.generatePortfolioID();
    try {
      Map<String, PortfolioData> translated = CSVToPortfolioAdapter.buildPortfolioData(
          portfolioData, new HashMap<>());
      jsonParser.writeToFile(newPFID + ".json", PORTFOLIO_DATA_PATH, new Gson().toJson(translated));
    } catch (IOException io) {
      return "Failed to create portfolio";
    }
    return "Created Portfolio with ID: " + newPFID;
  }

  @Override
  public String getPortfolioValue(String date, String pfID) throws ParseException {
//    String stockCountList;
    String pfData;
    LocalDate reqDate = LocalDate.parse(date);
    try {
      pfData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    } catch (FileNotFoundException e) {
      return "Sorry, could not find the portfolio with id "+pfID;
    }
    Map<String, PortfolioData> pfJsonData =  PortfolioDataAdapter.getObject(pfData);
    LocalDate oldestPurchaseDate = LocalDate.parse(Utility.getOldestDate(pfJsonData));

    if(reqDate.isBefore(oldestPurchaseDate)){
      return "0\nEnter date equal to or after "+oldestPurchaseDate.toString();
    }
    String stockCountList;
    try {
      stockCountList = this.getPortfolioCompositionByDate(date, pfID);
    } catch (FileNotFoundException e) {
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

    String portfolioValueCsvFormat = String.join("\n", portfolioValue);

    return portfolioValueCsvFormat;
  }

  @Override
  public String getPortfolioCompositionByDate(String date, String pfID)
      throws FileNotFoundException {
    String pfData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);

    Map<String, PortfolioData> filteredData = FilterPortfolio.getPortfolioBeforeDate(parsedPFData,
        date);
    String latestDate = Utility.getLatestDate(filteredData);
    List<StockData> stockDataForDate;
    try {
      stockDataForDate = filteredData.getOrDefault(latestDate, null).getStockList();
    } catch (NullPointerException n) {
      return "Sorry, no portfolio data found for given date/before it.";
    }
    if (stockDataForDate != null) {
      String stockDataCSV = PortfolioToCSVAdapter.buildStockQuantityList(stockDataForDate);

      return stockDataCSV;
    } else {
      return "Sorry, no stocks for given date.";
    }

  }

  @Override
  public String loadExternalPortfolio(String path) throws FileNotFoundException {
    return null;
  }

  @Override
  public String editExistingPortfolio(String pfID, String call) {
    String csvPFData;
    try {
      csvPFData =  jsonParser.readFile(pfID+".json",PORTFOLIO_DATA_PATH);
    }
    catch (FileNotFoundException f) {
      return "File not found!";
    }
    Map<String, PortfolioData> updatedPF = CSVToPortfolioAdapter.buildPortfolioData(
        call, PortfolioDataAdapter.getObject(csvPFData));

    try {
      jsonParser.writeToFile(pfID+".json",PORTFOLIO_DATA_PATH,new Gson().toJson(updatedPF));
    }
    catch (IOException io) {
      return "Unable to save Portfolio data";
    }
    return "Saved the updated portfolio!";

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

  public String showPerformance(String pfId, String startDate, String endDate)
      throws FileNotFoundException {
    String pfData = jsonParser.readFile(pfId + ".json", PORTFOLIO_DATA_PATH);
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);
    LocalDate localSD = LocalDate.parse(startDate);
    LocalDate localED = LocalDate.parse(endDate);

    if (localSD.isAfter(localED)) {
      return "Start date cannot be after End date";
    }
    String firstMostDate = Utility.getOldestDate(parsedPFData);
    if (localED.isBefore(LocalDate.parse(firstMostDate))) {
      return "End date is not in the portfolio. "
          + "Kindly try giving end date on or after " + Utility.getOldestDate(parsedPFData);

    }
    long months = 99999;
    long years = 99999;
    long days = ChronoUnit.DAYS.between(localSD, localED);

    if (days == 0) {
      return "Enter different dates for 2 ranges";
    } else if (days < 5) {
      return "Enter date range with more than 5 days";
    } else if (days > 31) {
      months = ChronoUnit.MONTHS.between(localSD, localED);
    }

    if (months < 5) {
      return "Enter date range with more than 5 months";
    } else if (months > 180) {
      years = ChronoUnit.YEARS.between(localSD, localED);
    }
    Performance performance = Performance.getBuilder().portfolioId(pfId).build();

    if (days >= 5 && days <= 31) {
      return performance.showPerformanceByDate(parsedPFData, startDate, endDate);
    } else if (months >= 5 && months <= 30) {
      return performance.showPerformanceByMonth(parsedPFData, startDate, endDate);
    } else if (months >= 15 && months <= 90) {
      return performance.showPerformanceByQuarter(parsedPFData, startDate, endDate);
    } else if (months >= 30 && months <= 180) {
      return performance.showPerformanceByHalfYear(parsedPFData, startDate, endDate);
    } else if (years <= 30) {
      return performance.showPerformanceByYear(parsedPFData, startDate, endDate);
    } else {
      return "Time range too big to display as graph.";
    }
  }

  public static void main(String args[]) throws FileNotFoundException {
    ModelOrchestratorV2 mv2 = new ModelOrchestratorV2();
    System.out.println(mv2.showPerformance("123455", "2021-09-11", "2021-11-11"));
  }
}
