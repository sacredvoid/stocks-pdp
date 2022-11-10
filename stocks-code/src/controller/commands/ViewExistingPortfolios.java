package controller.commands;

import controller.IPortfolioCommands;
import model.Orchestrator;

public class ViewExistingPortfolios extends APortfolioCommands {

  @Override
  public void go(Orchestrator m) {
    System.out.println("Showing Portfolios");
  }
}
