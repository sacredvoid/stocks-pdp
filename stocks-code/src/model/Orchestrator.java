package model;

import java.io.FileNotFoundException;
import java.text.ParseException;
import modelview.IModelView;
import org.jfree.chart.JFreeChart;

/**
 * The base model class for our application which defines the basic methods which other model like
 * classes need to implement.
 */
public interface Orchestrator extends IModelView {

  /**
   * Gets the portfolio data as string given a portfolio ID.
   *
   * @param portfolioID Portfolio ID (6 digit number)
   * @return portfolio data stored in the given PortfolioID csv file
   */
  String getLatestPortfolioComposition(String portfolioID) throws FileNotFoundException;

  /**
   * Takes in (Stocks,Quantity) CSV data, generates a random portfolio ID and then saves it (CSV
   * data) in a file.
   *
   * @param portfolioData CSV data as string containing Stock,Quantity
   * @return success/error message
   */
  String createPortfolio(String portfolioData);

  /**
   * generate a 6 digit random portfolio number for the application.
   *
   * @return 6-digit long string
   */
  String generatePortfolioID();

  /**
   * Fetches Portfolio Value as CSV Data in string format (stock,quantity,value) given a date and
   * portfolio data (in CSV). Since weekend data is unavailble for stocks, returns null if given
   * date is a weekend.
   *
   * @param date string like date in YYYY-MM-DD format
   * @param pfId string like CSV data of the portfolio (Stock,Quantity)
   * @return CSV Data (Stock,Quantity,Value) in string format/ null if date is weekend
   * @throws ParseException throws when it's unable to read the given date/data
   */
  String getPortfolioValue(String date, String pfId) throws ParseException;

  String getBuildGUIGraphStatus();

  String getCheckDateStatus();

  /**
   * Shows the existing portfolios in './app_data/PortfolioData' where our application is programmed
   * to store all Portfolios. Also creates the mentioned directory if not present.
   *
   * @return List of string containing the portfolio names/null if no portfolios found
   */
  String[] getExistingPortfolios();

  /**
   * Loads an external CSV: firstly reads the given path to CSV file and then writes it into the
   * './app_data/PortfolioData' directory with a generated Portfolio ID.
   *
   * @param path path to CSV existing on your file system
   * @return Success/error message depending on if the CSV load was successful or not
   * @throws FileNotFoundException throws when the given CSV file is not found/unable to reach it
   */
  String loadExternalPortfolio(String path) throws FileNotFoundException;

  String editExistingPortfolio(String pfID, String call);

  /**
   * Shows the line chart performance of a specified portfolio over the timespan provided<p></p> by
   * the user.
   *
   * @param pfId        Portfolio id of the portfolio
   * @param startDate   Starting date of the timespan
   * @param endDate     Ending date of the timespan
   * @return            performance of the portfolio for each timestamp in the form of stars
   */
  String showPerformance(String pfId, String startDate, String endDate)
      throws FileNotFoundException;

  String[] getCostBasis(String pfID, String date);

  String setCommissionFees(String commissionFees);

  JFreeChart generateTimeSeriesData(String pfID, String startDate, String endDate);

  String createDCAMap(String dcaInput);
}
