package controller.commands;

import java.io.FileNotFoundException;
import model.Orchestrator;

/**
 * The command to Load external portfolio (user CSV file to our platform's readable type ie. JSON).
 */
public class LoadExternalPortfolio extends APortfolioCommands {

  private String path;

  /**
   * Instantiates a new Load external portfolio with path to CSV.
   *
   * @param pathToPortfolioCSV the path to portfolio csv
   */
  public LoadExternalPortfolio(String pathToPortfolioCSV) {
    this.path = pathToPortfolioCSV;
  }

  @Override
  public void go(Orchestrator m) {
    try {
      setStatusMessage(m.loadExternalPortfolio(this.path));
    } catch (FileNotFoundException e) {
      setStatusMessage("Could not find a file at " + this.path);
    }
  }
}
