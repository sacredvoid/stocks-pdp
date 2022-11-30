package model;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.TreeMap;
import model.apistockops.PortfolioValue;
import model.dollarcostavg.DcaStrategyToCSV;
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 * Our model orchestrator class, as the name suggests, it is the link for the controller to call the
 * internal model classes and provide abstraction to the functionalities of our application. It
 * implements the Orchestrator interface.
 */
public class ModelOrchestratorV2 extends AOrchestrator {

  private FileOps jsonParser = new JSONFileOps();
  private final DateValidator dateValidator = new DateValidator();
  private float commissionFees = 1;
  public String rejectedTransactions;

  @Override
  public String getLatestPortfolioComposition(String portfolioID) throws FileNotFoundException {
    Map<String, PortfolioData> parsedPFData = getPFDataObject(portfolioID);
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
  public String getPortfolioValue(String date, String pfID) {
    LocalDate reqDate;
    try {
      reqDate = LocalDate.parse(date);
    } catch (DateTimeParseException e) {
      return "Sorry! Invalid date format!";
    }
    Map<String, PortfolioData> pfJsonData;
    try {
       pfJsonData = getPFDataObject(pfID);
    } catch (FileNotFoundException e) {
      return "Sorry, could not find the portfolio with id " + pfID;
    }

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
    if (!DateValidator.checkDateFormat(date)) {
      return "Sorry! Invalid date entered!";
    }
    Map<String, PortfolioData> parsedPFData = getPFDataObject(pfID);
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
//    String date = call.spl

    String filteredCall = getValidTransactions(call);
    if (filteredCall.isEmpty()) {
      return "Sorry, can make transactions on weekdays only. No changes made to PF ID: " + pfID;
    }

    try {
      csvPFData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    } catch (FileNotFoundException f) {
      return "Sorry, file not found!";
    }
    Map<String, PortfolioData> updatedPF = CSVToPortfolioAdapter.buildPortfolioData(
        filteredCall, PortfolioDataAdapter.getObject(csvPFData), this.commissionFees);

    try {
      jsonParser.writeToFile(pfID + ".json", PORTFOLIO_DATA_PATH, new Gson().toJson(updatedPF));
    } catch (IOException io) {
      return "Sorry, unable to save Portfolio data";
    }
    if (rejectedTransactions.isEmpty()) {
      return "Executed transactions:\n" + filteredCall + "\nSaved the updated Portfolio: " + pfID;
    } else {
      return "Executed transactions:\n" + filteredCall + "\nFailed transactions:\n"
          + rejectedTransactions
          + "\n since they were executed on a weekend. Please retry with a weekday!";
    }

  }

  @Override
  public String[] getCostBasis(String pfID, String date) {
    if (!DateValidator.checkDateFormat(date)) {
      return new String[]{"Sorry! Invalid date entered!"};
    }
    Map<String, PortfolioData> loadedPF;
    try {
       loadedPF = getPFDataObject(pfID);
    } catch (FileNotFoundException f) {
      return new String[]{"File not found!"};
    }
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
   * @param pfId      Portfolio id of the portfolio
   * @param startDate Starting date of the timespan
   * @param endDate   Ending date of the timespan
   * @return performance of the portfolio for each timestamp in the form of stars
   */

  public String showPerformance(String pfId, String startDate, String endDate)
      throws FileNotFoundException {
    if (!dateValidator.checkData(startDate) || !dateValidator.checkData(endDate)) {
      return "Sorry! Invalid date entered!";
    }
    TreeMap<String, Float> dataPoints;
    Map<String, PortfolioData> parsedPFData = getPFDataObject(pfId);
    LocalDate localSD = LocalDate.parse(startDate);
    LocalDate localED = LocalDate.parse(endDate);

    Long[] units = checkDates(localSD,localED,parsedPFData);
    if(units == null) {
      return checkDateStatus;
    }

    Performance performance = Performance.getBuilder().portfolioId(pfId).build();
    dataPoints = performance.getGraphDataPoints(parsedPFData, units[0], units[1], units[2], startDate,
        endDate);
    if (dataPoints == null) {
      return "Sorry, time range too big to display as graph. (More than 30 years)";
    } else {
      return performance.printGraph(dataPoints);
    }
  }

  // write method to fetch data points
  // write method to create timeseries data from those datapoints
  // make a new chart and send that new chart to view
  @Override
  public JFreeChart generateTimeSeriesData(String pfID, String startDate, String endDate) {
    Map<String, PortfolioData> parsedPFData;
    try {
       parsedPFData = getPFDataObject(pfID);
    }
    catch (FileNotFoundException e) {
      buildGUIGraphStatus = "Sorry, file not found!";
      return null;
    }

    Long[] units = checkDates(LocalDate.parse(startDate),LocalDate.parse(endDate),parsedPFData);
    if(units == null) {
      buildGUIGraphStatus = checkDateStatus;
      return null;
    }

    Performance performance = Performance.getBuilder().portfolioId(pfID).build();
    TreeMap<String, Float> dataPoints = performance.getGraphDataPoints(parsedPFData, units[0],units[1],units[2], startDate, endDate);
    if(dataPoints == null) {
      buildGUIGraphStatus = "Sorry, enough data not available";
      return null;
    }

    DefaultCategoryDataset stockDataTimeSeries = new DefaultCategoryDataset();
    for(Map.Entry<String, Float> entry: dataPoints.entrySet()) {
      stockDataTimeSeries.addValue(entry.getValue(), "Portfolio ID: "+pfID, entry.getKey());
    }
    return ChartFactory.createBarChart("Portfolio Performance", "Date Range", "Value",stockDataTimeSeries,
        PlotOrientation.VERTICAL , true , true , false);
  }

  public Long[] checkDates(LocalDate localSD, LocalDate localED,
      Map<String, PortfolioData> parsedPFData) {

    if (localSD.isAfter(localED)) {
      checkDateStatus = "Sorry, start date cannot be after end date";
      return null;
    }
    String firstMostDate = Utility.getOldestDate(parsedPFData);
    if (localED.isBefore(LocalDate.parse(firstMostDate))) {
      checkDateStatus = "Sorry, end date is not in the portfolio. "
          + "Kindly try giving end date on or after " + Utility.getOldestDate(parsedPFData);
      return null;

    }
    long months = 99999;
    long years = 99999;
    long days = ChronoUnit.DAYS.between(localSD, localED);

    if (days == 0) {
      checkDateStatus = "Sorry, please enter different dates for 2 ranges";
      return null;
    } else if (days < 5) {
      checkDateStatus = "Sorry, please enter date range with more than 5 days";
      return null;
    } else if (days > 31) {
      months = ChronoUnit.MONTHS.between(localSD, localED);
    }

    if (months <= 5) {
      checkDateStatus = "Sorry, please enter date range with more than 5 months";
      return null;
    }
    if (months > 180) {
      years = ChronoUnit.YEARS.between(localSD, localED);
    }
    return new Long[]{days, months, years};
  }


  @Override
  public String setCommissionFees(String commissionFees) {
    float value = Float.parseFloat(commissionFees);
    if (value < 0) {
      return "Cannot set negative commission";
    } else {
      this.commissionFees = value;
      return "Commission Fees set to: $" + value;
    }
  }

  public String createDCAPortfolio(Map<String, DollarCostAvgStrategy> strategyMap) {

    List<String> allTransactions = new ArrayList<>();
    for(Entry<String,DollarCostAvgStrategy> strategyRecord : strategyMap.entrySet()){
      DollarCostAvgStrategy strategy = strategyRecord.getValue();

      String startDate = strategy.getStartDate();
      String endDate = strategy.getEndDate();
      long recur = strategy.getRecurrCycle();
      String transactions="";
      try {
        transactions = DcaStrategyToCSV.
            getAllTransactions(strategy,startDate,recur,this.commissionFees,"");
      } catch (ParseException e) {
        //
      }
      allTransactions.add(transactions);
    }
    Map<String,PortfolioData> dummyPortfolio = CSVToPortfolioAdapter.buildPortfolioData(
       String.join("",allTransactions) ,new HashMap<>(),this.commissionFees);

    try {
      if (dummyPortfolio.size() > 0) {
        Map<String, DollarCostAveragePortfolio> dcapf = DollarCostAveragePortfolio.portfolioToDCA(
            dummyPortfolio, strategyMap);
        String newPFID = this.generatePortfolioID();

        new JSONFileOps().writeToFile(newPFID + ".json", "DcaPortfolioData",
            dcapf.toString());
        return "Created Portfolio with ID: " + newPFID;
      } else {
        return "Sorry, no data to create a portfolio with, date entered is invalid(weekend/future)";
      }
    }catch(IOException e){
      return "Sorry, Failed to create portfolio (write op failed)";
    }
  }

  @Override
  public String createDCAMap(String dcaInput) {
    String[] lines = dcaInput.split("\n");
    String dcaName = lines[0];
    float dcaInvestment = Float.parseFloat(lines[1]);
    long dcaRecurCycle = Long.parseLong(lines[2]);
    String dcaStartDate = lines[3];
    String dcaEndDate = lines[4];
    Map<String, Float> dcaStockPercentage = new HashMap<>();
    for (int i = 5;i<lines.length; i++) {
      String stock = lines[i].split(",")[0];
      String quantity = lines[i].split(",")[1];
      dcaStockPercentage.put(stock,Float.parseFloat(quantity));
    }
    DollarCostAvgStrategy dcaStrategy = new DollarCostAvgStrategy(dcaStockPercentage, dcaInvestment, dcaStartDate, dcaEndDate, dcaRecurCycle);
    Map<String, DollarCostAvgStrategy> dcaStrategyMap = new HashMap<>();
    dcaStrategyMap.put(dcaName,dcaStrategy);
    String status = createDCAPortfolio(dcaStrategyMap);
    return status;
  }

  public static void main(String args[]) throws IOException {
    ModelOrchestratorV2 m = new ModelOrchestratorV2();
    m.setCommissionFees(String.valueOf(2.0F));
    Map<String,DollarCostAvgStrategy> strategyMap = new LinkedHashMap<>();
    String test1Data =   new JSONFileOps().readFile("test.json", "PortfolioData");
    String test2Data = new JSONFileOps().readFile("test2.json", "PortfolioData");
//    String test3Data = new JSONFileOps().readFile("test3.json", "PortfolioData");
    strategyMap.put("strat1",new Gson().fromJson(test1Data, new TypeToken<DollarCostAvgStrategy>() {
    }.getType()));
    strategyMap.put("start2",new Gson().fromJson(test2Data, new TypeToken<DollarCostAvgStrategy>() {
    }.getType()));
    System.out.println(m.createDCAPortfolio(strategyMap));

  }

  private String getValidTransactions(String csv) {
    // Go through all rows of the data, get the date and then delete rows that have incorrect dates,
    // return error message to user
    String[] lines = csv.split("\n");
    StringJoiner newLines = new StringJoiner("\n");
    StringJoiner rejectedTrades = new StringJoiner("\n");
    for (String line : lines
    ) {
      String date = line.split(",")[2];
      if (dateValidator.checkData(date)) {
        newLines.add(line);
      } else {
        rejectedTrades.add(line);
      }
    }
    rejectedTransactions = rejectedTrades.toString();
    return newLines.toString();
  }

  private <T> Map<String, T> getPFDataObject(String pfID) throws FileNotFoundException {
    if(pfID.contains("-dca")) {
      // read it like dca

    }
    else {
      String pfData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
      return (Map<String, T>) PortfolioDataAdapter.getObject(pfData);
    }
  }

  public String getPredefinedStrategies() throws FileNotFoundException {
    // Read the strategies json file and get the strategy names and things
    Object o = jsonParser.readFile(".\\app_data\\strategies.json", "");
    System.out.println(o.toString());
    return null;
  }
}
