package controller.commands;

import java.io.FileNotFoundException;
import model.Orchestrator;

public class LoadExternalPortfolio extends APortfolioCommands {

  private String path;

  public LoadExternalPortfolio(String pathToPortfolioJSON) {
    System.out.println("Entered Load portfolio");
    this.path = pathToPortfolioJSON;
  }

  @Override
  public void go(Orchestrator m) {
    try {
      setStatusMessage(m.loadExternalPortfolio(this.path));
    } catch (FileNotFoundException e) {
      setStatusMessage("Could not find a file at "+ this.path);
    }
  }
}
