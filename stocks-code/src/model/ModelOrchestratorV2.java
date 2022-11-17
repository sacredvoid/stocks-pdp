package model;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import model.apiData.ApiDataStruct;
import model.fileops.CSVFileOps;
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

/**
 * Our model orchestrator class, as the name suggests, it is the link for the controller to call the
 * internal model classes and provide abstraction to the functionalities of our application. It
 * implements the Orchestrator interface.
 */
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
          portfolioData);
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
      return "Sorry, could not find the portfolio with id " + pfID;
    }
    Map<String, PortfolioData> pfJsonData = PortfolioDataAdapter.getObject(pfData);
    LocalDate oldestPurchaseDate = LocalDate.parse(Utility.getOldestDate(pfJsonData));

    if (reqDate.isBefore(oldestPurchaseDate)) {
      return "0\nEnter date equal to or after " + oldestPurchaseDate.toString();
    }
    String stockCountList;
    try {
      stockCountList = this.getPortfolioCompositionByDate(date, pfID);
    } catch (FileNotFoundException e) {
      return "Sorry, No stocks for given date"; // Get most recent stock list
    }

    if (stockCountList.contains("Sorry")) {
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

    int fileExtInd = path.lastIndexOf(".");
    String newPFID = generatePortfolioID();
    if (path.substring(fileExtInd + 1).equals("csv")) {
      CSVFileOps pw = new CSVFileOps();
      String readCSVData = pw.readFile(path, "");
      try {
        Map<String, PortfolioData> translated = CSVToPortfolioAdapter.buildPortfolioData(
            readCSVData.strip());
        jsonParser.writeToFile(newPFID + ".json", PORTFOLIO_DATA_PATH,
            new Gson().toJson(translated));
      } catch (IOException e) {
        return "Failed to load";
      }
      return "Created Portfolio with ID: " + newPFID;
    } else {
      return "File Not a CSV";
    }
  }

  @Override
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

}
