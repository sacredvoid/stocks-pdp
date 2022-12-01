package model;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import model.validation.DateValidator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 * Our model orchestrator class, as the name suggests, it is the link for the controller to call the
 * internal model classes and provide abstraction to the functionalities of our application. It
 * implements the Orchestrator interface.
 *
 * @param <T> the type parameter which extends PortfolioData class
 */
public class ModelOrchestratorV2<T extends PortfolioData> extends AOrchestrator {

  private FileOps jsonParser = new JSONFileOps();
  private final DateValidator dateValidator = new DateValidator();
  private float commissionFees = 1;
  /**
   * The Rejected/invalid transactions (future/selling more than present)
   */
  public String rejectedTransactions;

  @Override
  public String getLatestPortfolioComposition(String portfolioID) throws FileNotFoundException {
    Map<String, T> parsedPFData = getPFDataObject(portfolioID);
    String latestDate = Utility.getLatestDate(parsedPFData);
    String csvPortfolio = PortfolioToCSVAdapter.buildStockQuantityList(
        parsedPFData.get(latestDate).getStockList()
    );
    return csvPortfolio;
  }

  @Override
  public String createPortfolio(String portfolioData) {
    if (checkEmptyDataCSV(portfolioData)) {
      return commandStatus;
    }
    String newPFID = this.generatePortfolioID();
    try {
      Map<String, T> translated = CSVToPortfolioAdapter.buildPortfolioData(
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
    Map<String, T> pfJsonData;

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
    if (DateValidator.checkDateFormat(date)) {
      return "Sorry! Invalid date entered!";
    }
    Map<String, T> parsedPFData = getPFDataObject(pfID);
    List<StockData> stockDataForDate;
    if (parsedPFData.containsKey(date)) {
      stockDataForDate = parsedPFData.getOrDefault(date, null).getStockList();
    } else {
      Map<String, T> filteredData = FilterPortfolio.getPortfolioBeforeDate(parsedPFData,
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

  private boolean checkEmptyDataCSV(String csv) {
    String[] lines = csv.split("\n");
    for (String line : lines
    ) {
      String[] columns = line.split(",");
      for (String column : columns
      ) {
        if (column.isEmpty()) {
          commandStatus = "Missing data found!";
          return true;
        }
      }
    }
    return false;
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
        Map<String, T> translated = CSVToPortfolioAdapter.buildPortfolioData(
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
    if (checkEmptyDataCSV(call)) {
      return commandStatus;
    }

    Map<String, T> existingPFData;
    try {
      existingPFData = getPFDataObject(pfID);
    } catch (FileNotFoundException f) {
      return "Sorry, file not found!";
    }
    String filteredCall = getValidTransactions(call,
        existingPFData.get(Utility.getLatestDate(existingPFData)));
    if (filteredCall.isEmpty()) {
      return
          "Sorry, invalid transaction (weekend/sold more than current). No changes made to PF ID: "
              + pfID;
    }
    Map<String, T> updatedPF = CSVToPortfolioAdapter.buildPortfolioData(
        filteredCall, existingPFData, this.commissionFees);

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
          + "\n since you tried to sell more than present/weekend. Please retry!";
    }

  }

  @Override
  public String[] getCostBasis(String pfID, String date) {
    if (DateValidator.checkDateFormat(date)) {
      return new String[]{"Sorry! Invalid date entered!"};
    }
    Map<String, T> loadedPF;
    try {
      loadedPF = getPFDataObject(pfID);
    } catch (FileNotFoundException f) {
      return new String[]{"File not found!"};
    }
    T requiredEntry;
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
   * the user (Text UI).
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
    Map<String, T> parsedPFData = getPFDataObject(pfId);
    LocalDate localSD = LocalDate.parse(startDate);
    LocalDate localED = LocalDate.parse(endDate);

    Long[] units = checkDates(localSD, localED, parsedPFData);
    if (units == null) {
      return commandStatus;
    }

    Performance performance = Performance.getBuilder().portfolioId(pfId).build();
    dataPoints = performance.getGraphDataPoints(parsedPFData, units[0], units[1], units[2],
        startDate,
        endDate);
    if (dataPoints == null) {
      return "Sorry, time range too big to display as graph. (More than 30 years)";
    } else {
      return performance.printGraph(dataPoints);
    }
  }

  @Override
  public JFreeChart generateTimeSeriesData(String pfID, String startDate, String endDate) {
    Map<String, T> parsedPFData;
    try {
      parsedPFData = getPFDataObject(pfID);
    } catch (FileNotFoundException e) {
      buildGUIGraphStatus = "Sorry, file not found!";
      return null;
    }

    Long[] units = checkDates(LocalDate.parse(startDate), LocalDate.parse(endDate), parsedPFData);
    if (units == null) {
      buildGUIGraphStatus = commandStatus;
      return null;
    }

    Performance performance = Performance.getBuilder().portfolioId(pfID).build();
    TreeMap<String, Float> dataPoints = performance.getGraphDataPoints(parsedPFData, units[0],
        units[1], units[2], startDate, endDate);
    if (dataPoints == null) {
      buildGUIGraphStatus = "Sorry, enough data not available";
      return null;
    }

    DefaultCategoryDataset stockDataTimeSeries = new DefaultCategoryDataset();
    for (Map.Entry<String, Float> entry : dataPoints.entrySet()) {
      stockDataTimeSeries.addValue(entry.getValue(), "Portfolio ID: " + pfID, entry.getKey());
    }
    JFreeChart barChart = ChartFactory.createBarChart("Portfolio Performance", "Date Range",
        "Value", stockDataTimeSeries,
        PlotOrientation.VERTICAL, true, true, false);
    return barChart;
  }

  /**
   * Check dates long [ ].
   *
   * @param localSD      the local sd
   * @param localED      the local ed
   * @param parsedPFData the parsed pf data
   * @return the long [ ]
   */
  public Long[] checkDates(LocalDate localSD, LocalDate localED,
      Map<String, T> parsedPFData) {

    if (localSD.isAfter(localED)) {
      commandStatus = "Sorry, start date cannot be after end date";
      return null;
    }
    String firstMostDate = Utility.getOldestDate(parsedPFData);
    if (localED.isBefore(LocalDate.parse(firstMostDate))) {
      commandStatus = "Sorry, end date is not in the portfolio. "
          + "Kindly try giving end date on or after " + Utility.getOldestDate(parsedPFData);
      return null;

    }
    long months = 99999;
    long years = 99999;
    long days = ChronoUnit.DAYS.between(localSD, localED);

    if (days == 0) {
      commandStatus = "Sorry, please enter different dates for 2 ranges";
      return null;
    } else if (days > 31) {
      months = ChronoUnit.MONTHS.between(localSD, localED);
    }
    if (months > 180) {
      years = ChronoUnit.YEARS.between(localSD, localED);
    }
    return new Long[]{days, months, years};
  }


  @Override
  public String setCommissionFees(String commissionFees) {
    float value;
    try {
      value = Float.parseFloat(commissionFees);
    } catch (NumberFormatException e) {
      return "Please enter a number";
    }
    if (value < 0) {
      return "Cannot set negative commission";
    } else {
      this.commissionFees = value;
      return "Commission Fees set to: $" + value;
    }
  }

  @Override
  public String createDCAPortfolio(String dcaInput) {

    Map<String, DollarCostAvgStrategy> strategyMap = createDCAMap(dcaInput);
    if (strategyMap == null) {
      return commandStatus;
    }

    List<String> allTransactions = new ArrayList<>();
    for(Entry<String,DollarCostAvgStrategy> strategyRecord : strategyMap.entrySet()){
      allTransactions.add(getStrategyTransactions(strategyRecord.getValue(),strategyRecord.getValue().getStartDate()));

    }
    Map<String, T> dummyPortfolio = CSVToPortfolioAdapter.buildPortfolioData(
        String.join("", allTransactions), new HashMap<>(), this.commissionFees);

    try {
      if (dummyPortfolio.size() > 0) {
        Map<String, T> dcapf = DollarCostAveragePortfolio.portfolioToDCA(
            dummyPortfolio, strategyMap);
        String newPFID = this.generatePortfolioID();

        new JSONFileOps().writeToFile(newPFID + "-dca.json", "PortfolioData",
            dcapf.toString());
        return "Created SIP Portfolio with ID: " + newPFID +"-dca";
      } else {
        return "Sorry, no data to create a portfolio with, date entered is invalid(weekend/future)";
      }
    } catch (IOException e) {
      return "Sorry, Failed to create portfolio (write op failed)";
    }
  }

  private Map<String, DollarCostAvgStrategy> createDCAMap(String dcaInput) {
    String[] lines = dcaInput.split("\n");
    for (int i = 0; i < lines.length; i++) {
      if (lines[i].isEmpty()) {
        commandStatus = "Missing data for DCA!";
      } else if (lines[i].contains(",")) {
        String[] stockWeightageSplit = lines[i].split(",");
        if (stockWeightageSplit.length == 0) {
          commandStatus = "Missing stock-weight data!";
          return null;
        }
      }
    }
    String dcaName = lines[0];
    float dcaInvestment;
    long dcaRecurCycle;
    try {
      dcaInvestment = Float.parseFloat(lines[2]);
      dcaRecurCycle = Long.parseLong(lines[1]);
    } catch (NumberFormatException e) {
      commandStatus = "Investment/Cycle not a number!";
      return null;
    }
    String dcaStartDate = lines[3];
    String dcaEndDate = lines[4];
    if (dcaEndDate.equalsIgnoreCase("null")) {
      dcaEndDate = "";
    }
    Map<String, Float> dcaStockPercentage = new HashMap<>();
    for (int i = 5; i < lines.length; i++) {
      String stock = lines[i].split(",")[0];
      String weightage = lines[i].split(",")[1];
      dcaStockPercentage.put(stock, Float.parseFloat(weightage));
    }
    DollarCostAvgStrategy dcaStrategy = new DollarCostAvgStrategy(dcaStockPercentage, dcaInvestment,
        dcaStartDate, dcaEndDate, dcaRecurCycle);
    Map<String, DollarCostAvgStrategy> dcaStrategyMap = new HashMap<>();
    dcaStrategyMap.put(dcaName, dcaStrategy);
    return dcaStrategyMap;
  }

  private String getStrategyTransactions(DollarCostAvgStrategy strategy, String reqDate){
    long recur = strategy.getRecurrCycle();
    String transactions = "";
    try {
      transactions = DcaStrategyToCSV.
          getAllTransactions(strategy,reqDate,recur,this.commissionFees,"");

    } catch (ParseException ignored) {
      //
    }
    return transactions;
  }

  @Override
  public String existingPortfolioToDCAPortfolio(String pfID, String dcaData) {
    Map<String, DollarCostAvgStrategy> strategyMap = createDCAMap(dcaData);
    if (strategyMap == null) {
      return commandStatus;
    }

    Map<String, T> readDcaPf;
    try {
      readDcaPf = getPFDataObject(pfID);
    } catch (FileNotFoundException e) {
      return "File not found!";
    }
    List<String> allTransactions = new ArrayList<>();
    for (Entry<String, DollarCostAvgStrategy> strategyRecord : strategyMap.entrySet()
    ) {
      DollarCostAvgStrategy strategy = strategyRecord.getValue();
      allTransactions.add(getStrategyTransactions(strategy,strategy.getStartDate()));
    }
    Map<String, PortfolioData> tempPFData = DollarCostAveragePortfolio.dcaToPortfolio((Map<String, DollarCostAveragePortfolio>)readDcaPf);
    Map<String, T> updatedPF = CSVToPortfolioAdapter.buildPortfolioData(
        String.join("", allTransactions), readDcaPf, this.commissionFees);

    Map<String,DollarCostAvgStrategy> previousStrategies = new HashMap<>();
    Map<String,DollarCostAvgStrategy> finalStrategiesSet = new HashMap<>(strategyMap);
    if(pfID.contains("-dca")){
      for (Entry<String,T> entry : readDcaPf.entrySet()
      ) {
//        ((DollarCostAveragePortfolio) dollarCostAveragePortfolio.get(
//            Utility.getLatestDate(dollarCostAveragePortfolio))).getDcaStrategy()
        DollarCostAveragePortfolio temp = (DollarCostAveragePortfolio) entry.getValue();
        Map<String,DollarCostAvgStrategy> prevStrategyMap = temp.getDcaStrategy();
        System.out.println(prevStrategyMap.toString());
        for (Entry<String,DollarCostAvgStrategy> individualPrevStratMap : prevStrategyMap.entrySet()
        ) {
//          String tempStratName = individualPrevStratMap.getKey();
//          DollarCostAvgStrategy tempStrat = individualPrevStratMap.getValue()
          previousStrategies.put(individualPrevStratMap.getKey(), individualPrevStratMap.getValue());
        }
      }
      finalStrategiesSet.putAll(previousStrategies);
    }
    Map<String, T> result = DollarCostAveragePortfolio.portfolioToDCA(
        updatedPF, finalStrategiesSet);
    try {
      String newFileName;
      if (pfID.contains("-dca")) {
        newFileName = pfID;
      } else {
        newFileName = pfID + "-dca";
      }
      new JSONFileOps().writeToFile(newFileName + ".json", "PortfolioData",
          result.toString());

      return "Modified Portfolio " + pfID + " to SIP portfolio " + newFileName;

    } catch (IOException e) {
      return "Sorry, Failed to modify portfolio (write op failed)";
    }
  }

  private String getValidTransactions(String csv, T pfData) {
    // Go through all rows of the data, get the date and then delete rows that have incorrect dates,
    // return error message to user
    String[] lines = csv.split("\n");
    StringJoiner newLines = new StringJoiner("\n");
    StringJoiner rejectedTrades = new StringJoiner("\n");
    for (String line : lines
    ) {
      String date = line.split(",")[2];
      String call = line.split(",")[3];
      String stockName = line.split(",")[0];
      String quantity = line.split(",")[1];
      if (call.equals("SELL")) {
        if (!checkSharesForSell(pfData, stockName, Float.parseFloat(quantity))) {
          rejectedTrades.add(line);
          continue;
        }
      }
      if (dateValidator.checkData(date)) {
        newLines.add(line);
      } else {
        rejectedTrades.add(line);
      }
    }
    rejectedTransactions = rejectedTrades.toString();
    return newLines.toString();
  }

  private boolean checkSharesForSell(T data, String stockName, float quantityToCheck) {
    return data.getQuantity(stockName) >= quantityToCheck;
  }

  private Map<String, T> getPFDataObject(String pfID) throws FileNotFoundException {
    String pfData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    if (pfID.contains("-dca")) {
      Type generic = new TypeToken<HashMap<String, DollarCostAveragePortfolio>>() {
      }.getType();
      return new Gson().fromJson(pfData, generic);
    } else {
      Type generic = new TypeToken<HashMap<String, PortfolioData>>() {
      }.getType();
      return PortfolioDataAdapter.getObject(pfData, generic);
    }
  }

  @Override
  public String getPredefinedStrategies(String pfID) throws FileNotFoundException {
    // Read the strategies json file and get the strategy names and things
    StringJoiner strategyInfo = new StringJoiner("\n");
    Map<String, T> dollarCostAveragePortfolio = getPFDataObject(pfID);
    Map<String, DollarCostAvgStrategy> strategies;
    try {
      strategies = ((DollarCostAveragePortfolio) dollarCostAveragePortfolio.get(
          Utility.getLatestDate(dollarCostAveragePortfolio))).getDcaStrategy();
    } catch (ClassCastException e) {
      return "Not a DCA Portfolio!";
    }
    if (strategies != null) {
      for (Entry<String, DollarCostAvgStrategy> strategyRecord : strategies.entrySet()
      ) {
        strategyInfo.add("Strategy Name: " + strategyRecord.getKey());
        DollarCostAvgStrategy strategy = strategyRecord.getValue();
        strategyInfo.add(strategy.toString());
      }
      return strategyInfo.toString();
    } else {
      return "No strategies found!";
    }
  }
}
