package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import model.fileops.CSVFileOps;
import model.validation.DateValidator;
import model.validation.IDataValidator;

/**
 * Our model orchestrator class, as the name suggests, it is the link for the controller to call the
 * internal model classes and provide abstraction to the functionalities of our application. It
 * implements the Orchestrator interface.
 */
public class ModelOrchestrator extends AOrchestrator {

  private IDataValidator dataValidator = new DateValidator();

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
    if (dataValidator.checkData(date)) {
      // Takes in portfolio data and returns the value of the portfolio on given date
      StringBuilder finalStockData = new StringBuilder();

      List<String> output1;
      output1 = PortfolioValue.getBuilder()
          .stockCountList(data)
          .date(date)
          .build()
          .completePortfolioValue();

      for (String s : output1
      ) {
        finalStockData.append(s).append("\n");
      }
      return finalStockData.toString();
    } else {
      return null;
    }
  }

  @Override
  public String getPortfolioValueByID(String date, String pfID) throws FileNotFoundException {
    return null;
  }

  /**
   * Loads an external CSV: firstly reads the given path to CSV file and then writes it into the
   * './app_data/PortfolioData' directory with a generated Portfolio ID.
   *
   * @param path path to CSV existing on your file system
   * @return Success/error message depending on if the CSV load was successful or not
   * @throws FileNotFoundException throws when the given CSV file is not found/unable to reach it
   */
  @Override
  public String loadExternalPortfolio(String path) throws FileNotFoundException {
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