package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

/**
 * Our model orchestrator class, as the name suggests, it is the link for the controller to call the
 * internal model classes and provide abstraction to the functionalities of our application. It
 * implements the Orchestrator interface.
 */
public class ModelOrchestrator implements Orchestrator {

  private final String osSep = OSValidator.getOSSeparator();
  private final String PORTFOLIO_DATA_PATH = String.format(
      "%sPortfolioData%s", osSep, osSep
  );

  private final String acceptableDateLimit = "2022-11-01";

  private CSVFileOps pw = new CSVFileOps();

  /**
   * Gets the portfolio data as string given a portfolio ID.
   *
   * @param portfolioID Portfolio ID (6 digit number)
   * @return portfolio data stored in the given PortfolioID csv file
   */
  public String getPortfolio(String portfolioID) throws FileNotFoundException {

    // if portfolio exists, return data
    // else throw exception
    String portfolioFile = portfolioID + ".csv";
    return pw.readFile(portfolioFile, PORTFOLIO_DATA_PATH);
  }

  /**
   * Takes in (Stocks,Quantity) CSV data, generates a random portfolio ID and then saves it (CSV
   * data) in a file.
   *
   * @param portfolioData CSV data as string containing Stock,Quantity
   * @return success/error message
   */
  public String createPortfolio(String portfolioData) {
    String newPortfolioID = generatePortfolioID();

    try {
      pw.writeToFile(newPortfolioID + ".csv", PORTFOLIO_DATA_PATH, portfolioData.strip());
    } catch (IOException e) {
      return "Error while writing to file: " + e.getMessage();
    }
    return String.format("Portfolio ID: %s Saved!", newPortfolioID);
  }

  /**
   * generate a 6 digit random portfolio number for the application
   *
   * @return 6-digit long string
   */
  public String generatePortfolioID() {
    int leftLimit = 48; // letter 'a'
    int rightLimit = 57; // letter 'z'
    int targetStringLength = 6;
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  /**
   * Fetches Portfolio Value as CSV Data in string format (stock,quantity,value) given a date and
   * portfolio data (in CSV). Since weekend data is unavailble for stocks, returns null if given
   * date is a weekend.
   *
   * @param date string like date in YYYY-MM-DD format
   * @param data string like CSV data of the portfolio (Stock,Quantity)
   * @return CSV Data (Stock,Quantity,Value) in string format/ null if date is weekend
   * @throws ParseException throws when it's unable to read the given date/data
   */
  public String getPortfolioValue(String date, String data) throws ParseException {

    // Check if Date is a weekend
    if (isValidDate(date)) {
      // Takes in portfolio data and returns the value of the portfolio on given date
      StringBuilder finalStockData = new StringBuilder();
//    Date dateNew = DateFormatter.getDate(date);
      List<String> output1;
      if (date.isEmpty()) {
        output1 = PortfolioValue.getBuilder()
            .stockCountList(data)
            .build()
            .completePortfolioValue();
      } else {
        output1 = PortfolioValue.getBuilder()
            .stockCountList(data)
            .date(date)
            .build()
            .completePortfolioValue();
      }

      for (String s : output1
      ) {
        finalStockData.append(s).append("\n");
      }
      return finalStockData.toString();
    } else {
      return null;
    }
  }

  /**
   * Shows the existing portfolios in './app_data/PortfolioData' where our application is programmed
   * to store all Portfolios. Also creates the mentioned directory if not present.
   *
   * @return List of string containing the portfolio names/null if no portfolios found
   */
  public String[] showExistingPortfolios() {
    File f = new File("." + osSep + "app_data" + osSep + PORTFOLIO_DATA_PATH);
    if (!f.isDirectory()) {
      f.mkdirs();
    }
    String[] filesList = f.list();
      if (filesList.length == 0) {
          return null;
      } else {
          return filesList;
      }
  }

  /**
   * Checks if the given date (YYYY-MM-DD) is a weekend and returns true if it is, false otherwise.
   *
   * @param inputDate Date type date in YYYY-MM-DD format
   * @return true/false given if the date is weekend or not
   */
  private boolean isWeekend(Date inputDate) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(inputDate);
    int day = cal.get(Calendar.DAY_OF_WEEK);
    return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
  }

  private boolean isValidDate(String date) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date inputDate = formatter.parse(date);
    LocalDateTime now = LocalDateTime.now();
    Date baseDate = formatter.parse(String.valueOf(now));
    return !(isWeekend(inputDate) || isFuture(baseDate,inputDate));
  }

  private boolean isFuture(Date limit, Date input) {
    return input.after(limit);
  }

  /**
   * Loads an external CSV: firstly reads the given path to CSV file and then writes it into the
   * './app_data/PortfolioData' directory with a generated Portfolio ID.
   *
   * @param path path to CSV existing on your file system
   * @return Success/error message depending on if the CSV load was successful or not
   * @throws FileNotFoundException throws when the given CSV file is not found/unable to reach it
   */
  public String loadExternalCSV(String path) throws FileNotFoundException {
    String readCSVData = pw.readFile(path, "");
    String portfolioID = generatePortfolioID();
    int fileExtInd = path.lastIndexOf(".");
      if (path.substring(fileExtInd + 1).equals("csv")) {
          try {
              pw.writeToFile(portfolioID + ".csv", PORTFOLIO_DATA_PATH, readCSVData.strip());
          } catch (IOException e) {
              return "Failed to load";
          }
          return portfolioID;
      } else {
          return "File Not a CSV";
      }
  }
}
