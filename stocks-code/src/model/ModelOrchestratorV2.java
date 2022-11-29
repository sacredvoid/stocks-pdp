package model;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import model.apistockops.PortfolioValue;
import model.dollarcostavg.DollarCostAvgStrategy;
import model.fileops.CSVFileOps;
import model.fileops.FileOps;
import model.fileops.JSONFileOps;
import model.performance.Performance;
import model.portfolio.CSVToPortfolioAdapter;
import model.portfolio.DollarCostAveragePortfolio;
import model.portfolio.PortfolioData;
import model.portfolio.PortfolioDataAdapter;
import model.portfolio.PortfolioToCSVAdapter;
import model.portfolio.StockData;
import model.portfolio.Utility;
import model.portfolio.filters.FilterPortfolio;
import java.time.temporal.ChronoUnit;
import model.validation.DateValidator;

/**
 * Our model orchestrator class, as the name suggests, it is the link for the controller to call the
 * internal model classes and provide abstraction to the functionalities of our application. It
 * implements the Orchestrator interface.
 */
public class ModelOrchestratorV2 extends AOrchestrator {

  private FileOps jsonParser = new JSONFileOps();
  private final DateValidator dateValidator = new DateValidator();
  private float commissionFees = 1;

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
      return "Sorry, No data provided. No portfolio was created";
    }
    String newPFID = this.generatePortfolioID();
    try {
      Map<String, PortfolioData> translated = CSVToPortfolioAdapter.buildPortfolioData(
          portfolioData, new HashMap<>(), this.commissionFees);
      if (translated.size() > 0) {
        jsonParser.writeToFile(newPFID + ".json", PORTFOLIO_DATA_PATH,
            new Gson().toJson(translated));
        return "Created Portfolio with ID: " + newPFID;
      } else {
        return "Sorry, no data to create a portfolio with, date entered is invalid(weekend/future)";
      }
    } catch (IOException io) {
      return "Sorry, Failed to create portfolio (write op failed)";
    }

  }

  @Override
  public String getPortfolioValue(String date, String pfID) throws ParseException {
    String pfData;
    if(!dateValidator.checkData(date)) {
      return "Sorry! Invalid date entered!";
    }
    LocalDate reqDate = LocalDate.parse(date);
    try {
      pfData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    } catch (FileNotFoundException e) {
      return "Sorry, could not find the portfolio with id " + pfID;
    }

    Map<String, PortfolioData> pfJsonData = PortfolioDataAdapter.getObject(pfData);
    LocalDate oldestPurchaseDate = LocalDate.parse(Utility.getOldestDate(pfJsonData));

    if (reqDate.isBefore(oldestPurchaseDate)) {
      return "0. No Portfolio Data before given date. Sorry! enter date equal to or after "
          + oldestPurchaseDate.toString();
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
    if(!dateValidator.checkData(date)) {
      return "Sorry! Invalid date entered!";
    }
    String pfData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);
    List<StockData> stockDataForDate;
    if (parsedPFData.containsKey(date)) {
      stockDataForDate = parsedPFData.getOrDefault(date, null).getStockList();
    } else {
      Map<String, PortfolioData> filteredData = FilterPortfolio.getPortfolioBeforeDate(parsedPFData,
          date);
      String latestDate = Utility.getLatestDate(filteredData);
      try {
        stockDataForDate = filteredData.getOrDefault(latestDate, null).getStockList();
      } catch (NullPointerException n) {
        return "Sorry, no portfolio data found for given date/before it.";
      }
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
      String[] readCSVDataLines = readCSVData.split("\n");
      StringBuilder sb = new StringBuilder();
      int i;
      for (i = 0; i < readCSVDataLines.length; i++) {
        sb.append(readCSVDataLines[i] + ",BUY\n");
      }

      try {
        Map<String, PortfolioData> translated = CSVToPortfolioAdapter.buildPortfolioData(
            sb.toString().strip(), new HashMap<>(), this.commissionFees);
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
  public String editExistingPortfolio(String pfID, String call) {
    String csvPFData;
    try {
      csvPFData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    } catch (FileNotFoundException f) {
      return "Sorry, file not found!";
    }
    Map<String, PortfolioData> updatedPF = CSVToPortfolioAdapter.buildPortfolioData(
        call, PortfolioDataAdapter.getObject(csvPFData), this.commissionFees);

    try {
      jsonParser.writeToFile(pfID + ".json", PORTFOLIO_DATA_PATH, new Gson().toJson(updatedPF));
    } catch (IOException io) {
      return "Sorry, unable to save Portfolio data";
    }
    return "Saved the updated Portfolio:"+pfID;

  }

  @Override
  public String[] getCostBasis(String pfID, String date) {
    String csvPFData;
    if(!dateValidator.checkData(date)) {
      return new String[]{"Sorry! Invalid date entered!"};
    }
    try {
      csvPFData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    } catch (FileNotFoundException f) {
      return new String[]{"File not found!"};
    }
    Map<String, PortfolioData> loadedPF = PortfolioDataAdapter.getObject(csvPFData);
    PortfolioData requiredEntry;
    if (loadedPF.containsKey(date)) {
      requiredEntry = loadedPF.get(date);
    } else {
      String latestDateBeforeGivenDate = Utility.getLatestDate(FilterPortfolio
          .getPortfolioBeforeDate(loadedPF, date));
      if (latestDateBeforeGivenDate.contains("No data found to sort")) {
        return new String[]{"No data before given date"};
      }
      requiredEntry = loadedPF.get(latestDateBeforeGivenDate);
    }

    float totalInvested = requiredEntry.getTotalInvested();
    float totalCommission = requiredEntry.getTotalCommission();
    float totalEarned = requiredEntry.getTotalEarned();
    float totalCostBasis = totalInvested + totalCommission;

    return new String[]{
        String.valueOf(totalInvested),
        String.valueOf(totalCommission),
        String.valueOf(totalEarned),
        String.valueOf(totalCostBasis)
    };

  }

  /**
   * Shows the line chart performance of a specified portfolio over the timespan provided<p></p> by
   * the user.
   *
   * @param pfId        Portfolio id of the portfolio
   * @param startDate   Starting date of the timespan
   * @param endDate     Ending date of the timespan
   * @return            performance of the portfolio for each timestamp in the form of stars
   */

  public String showPerformance(String pfId, String startDate, String endDate)
      throws FileNotFoundException {
    if(!dateValidator.checkData(startDate) || !dateValidator.checkData(endDate)) {
      return "Sorry! invalid date entered!";
    }
    String pfData = jsonParser.readFile(pfId + ".json", PORTFOLIO_DATA_PATH);
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);
    LocalDate localSD = LocalDate.parse(startDate);
    LocalDate localED = LocalDate.parse(endDate);

    if (localSD.isAfter(localED)) {
      return "Sorry, start date cannot be after end date";
    }
    String firstMostDate = Utility.getOldestDate(parsedPFData);
    if (localED.isBefore(LocalDate.parse(firstMostDate))) {
      return "Sorry, end date is not in the portfolio. "
          + "Kindly try giving end date on or after " + Utility.getOldestDate(parsedPFData);

    }
    long months = 99999;
    long years = 99999;
    long days = ChronoUnit.DAYS.between(localSD, localED);

    if (days == 0) {
      return "Sorry, please enter different dates for 2 ranges";
    } else if (days < 5) {
      return "Sorry, please enter date range with more than 5 days";
    } else if (days > 31) {
      months = ChronoUnit.MONTHS.between(localSD, localED);
    }

    if (months < 5) {
      return "Sorry, please enter date range with more than 5 months";
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
      return "Sorry, time range too big to display as graph. (More than 30 years)";
    }
  }

  @Override
  public String setCommissionFees(String commissionFees) {
    float value = Float.parseFloat(commissionFees);
    if (value < 0) {
      return "Cannot set negative commission";
    } else {
      this.commissionFees = value;
      return "Commission Fees set to: " + value;
    }
  }

  public String createDCAPortfolio(Map<String, DollarCostAvgStrategy> strategyMap)
      throws IOException {
    List<StockData> stockDataList = new ArrayList<>();
    Map<String,Float> tempStockQtyMap = new HashMap<>();

    float totalInvested =0.0F;
    float totalCommision = 0.0F;

    for (Entry<String,DollarCostAvgStrategy> strategyRecord : strategyMap.entrySet()
    ) {
      DollarCostAvgStrategy strategy = strategyRecord.getValue();
      String stockQtyList  = strategy.dcgStockQtyList(strategy.getStartDate(),
          strategy.getRecurrInvAmt()-(this.commissionFees * strategy.getStockPercentMap().size()));

      List<String> stockQtyArrayList = List.of(stockQtyList.split("\n"));

      for(String stockQty: stockQtyArrayList){
        String stockName = stockQty.split(",")[0];
        Float qty = Float.valueOf(stockQty.split(",")[1]);
        if(!tempStockQtyMap.containsKey(stockQty.split(",")[0])){
          tempStockQtyMap.put(stockName, qty);
        }
        else{
          tempStockQtyMap.put(stockName,tempStockQtyMap.get(stockName)+qty);
        }

      }

      totalCommision += this.commissionFees*strategy.getStockPercentMap().size();
      totalInvested+=strategy.getRecurrInvAmt()-this.commissionFees*strategy.getStockPercentMap().size();

    }

    for (Entry<String,Float> entry: tempStockQtyMap.entrySet()
    ) {
      stockDataList.add(new StockData(entry.getKey(),entry.getValue()));
    }

    DollarCostAveragePortfolio newDCApf = new DollarCostAveragePortfolio(stockDataList,
        totalInvested,totalCommision,0.0F,strategyMap);

    String newPFID = this.generatePortfolioID();
    new JSONFileOps().writeToFile(newPFID+".json","DcaPortfolioData",newDCApf.toString());
    return "Created Portfolio with ID: " + newPFID;

  }

  public static void main(String args[]) throws IOException {
    ModelOrchestratorV2 m = new ModelOrchestratorV2();
    m.setCommissionFees(String.valueOf(2.0F));
    Map<String,DollarCostAvgStrategy> strategyMap = new LinkedHashMap<>();
    String test1Data =   new JSONFileOps().readFile("test.json", "PortfolioData");
    String test2Data = new JSONFileOps().readFile("test2.json", "PortfolioData");
    String test3Data = new JSONFileOps().readFile("test3.json", "PortfolioData");
    strategyMap.put("strat1",new Gson().fromJson(test1Data, new TypeToken<DollarCostAvgStrategy>() {
    }.getType()));
    strategyMap.put("start2",new Gson().fromJson(test2Data, new TypeToken<DollarCostAvgStrategy>() {
    }.getType()));

    strategyMap.put("start3",new Gson().fromJson(test3Data, new TypeToken<DollarCostAvgStrategy>(){}.getType()));
    m.createDCAPortfolio(strategyMap);
  }
}
