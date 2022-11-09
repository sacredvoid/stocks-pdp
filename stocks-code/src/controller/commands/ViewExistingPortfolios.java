package controller.commands;

import controller.IPortfolioCommands;
import model.Orchestrator;

public class ViewExistingPortfolios implements IPortfolioCommands {

  @Override
  public void go(Orchestrator m) {
    System.out.println("Showing Portfolios");
  }
}
