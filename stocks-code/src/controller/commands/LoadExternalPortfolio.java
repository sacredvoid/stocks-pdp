package controller.commands;

import model.Orchestrator;

public class LoadExternalPortfolio extends APortfolioCommands {

  private String path;

  public LoadExternalPortfolio(String pathToPortfolioJSON) {
    System.out.println("Entered Load portfolio");
    this.path = pathToPortfolioJSON;
  }

  @Override
  public void go(Orchestrator m) {

  }
}
