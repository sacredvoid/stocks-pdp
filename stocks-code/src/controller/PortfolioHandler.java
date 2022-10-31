package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import model.CSVFileOps;
import model.DateFormatter;
import model.OSValidator;
import model.PortfolioValue;

/**
 * Class to get and create user portfolios. Should contain the workflow to talk to model and send
 * back info to view.
 */
public class PortfolioHandler {
  private final String osSep = OSValidator.getOSSeparator();
  private final String PORTFOLIO_DATA_PATH = String.format(
      "%sPortfolioData%s",osSep,osSep
  );

  private CSVFileOps pw = new CSVFileOps();

  /**
   * This gets the portfolio data as string given a portfolio ID
   * @param portfolioID string input from user
   * @return portfolio data (could be as List<List<String>>)
   */
  public String getPortfolio(String portfolioID) throws FileNotFoundException {

    // if portfolio exists, return data
    // else throw exception
    String portfolioFile = portfolioID+".csv";
    return pw.readFile(portfolioFile,PORTFOLIO_DATA_PATH);
  }

  /**
   * This takes in Stocks, Quantity from user, saves it in a file, also calls generatePortfolioID
   * @param portfolioData list of list of string containing stock name, qt
   * @return string with the created portfolio data
   */
  public String createPortfolio(String portfolioData) {
    String newPortfolioID = generatePortfolioID();

    try {
      pw.writeToFile(newPortfolioID+".csv",PORTFOLIO_DATA_PATH,portfolioData.strip());
    }
    catch (IOException e) {
      return "Error while writing to file: "+e.getMessage();
    }
    return String.format("Portfolio ID: %s Saved!",newPortfolioID);
  }

  /**
   * generate a 6 digit random portfolio number for the user
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

  public String getPortfolioValue(String date, String data) throws ParseException {
    // Takes in portfolio data and returns the value of the portfolio on given date
    StringBuilder finalStockData = new StringBuilder();
//    Date dateNew = DateFormatter.getDate(date);
    List<String> output1;
    if (date.isEmpty()) {
      output1 = PortfolioValue.getBuilder()
          .stockCountList(data)
          .build()
          .completePortfolioValue();
    }
    else {
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
  }

  public String[] showExistingPortfolios() {
    File f = new File("."+osSep+"app_data"+osSep+PORTFOLIO_DATA_PATH);
    return f.list();
  }


}
