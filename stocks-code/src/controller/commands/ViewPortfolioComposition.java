package controller.commands;

import controller.IPortfolioCommands;
import model.Orchestrator;

public class ViewPortfolioComposition extends APortfolioCommands {

  private String portfolioID;

  public ViewPortfolioComposition(String portfolioID) {
    this.portfolioID = portfolioID;
  }

  @Override
  public void go(Orchestrator m) {
    System.out.println("Showing you the portfolio composition!");
  }
}
