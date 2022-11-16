package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

public abstract class AOrchestrator implements Orchestrator {

  protected String osSep = OSValidator.getOSSeparator();
  protected final String PORTFOLIO_DATA_PATH = String.format(
      "%sPortfolioData%s", osSep, osSep
  );

  /**
   * Shows the existing portfolios in './app_data/PortfolioData' where our application is programmed
   * to store all Portfolios. Also creates the mentioned directory if not present.
   *
   * @return List of string containing the portfolio names/null if no portfolios found
   */

  @Override
  public String[] getExistingPortfolios() {
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
   * generate a 6 digit random portfolio number for the application.
   *
   * @return 6-digit long string
   */
  @Override
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

}
