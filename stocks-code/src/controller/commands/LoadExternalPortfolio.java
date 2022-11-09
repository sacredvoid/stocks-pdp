package controller.commands;

import controller.IPortfolioCommands;
import java.util.Scanner;
import model.ModelOrchestrator;
import model.Orchestrator;

public class LoadExternalPortfolio implements IPortfolioCommands {

  private String path;

  public LoadExternalPortfolio(String pathToPortfolioJSON) {
    System.out.println("Entered Load portfolio");
    this.path = pathToPortfolioJSON;
  }

  @Override
  public void go(Orchestrator m) {

  }
}
