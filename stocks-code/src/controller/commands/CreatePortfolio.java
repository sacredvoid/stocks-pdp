package controller.commands;

import controller.IPortfolioCommands;
import model.ModelOrchestrator;
import model.Orchestrator;

public class CreatePortfolio implements IPortfolioCommands {

  private String stockData;

  public CreatePortfolio(String data) {
    this.stockData = data;
  }
  @Override
  public void go(Orchestrator m) {

  }
}
