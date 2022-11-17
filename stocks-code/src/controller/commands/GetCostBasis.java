package controller.commands;

import model.Orchestrator;

public class GetCostBasis extends APortfolioCommands {

  private String pfID;
  private String date;

  public GetCostBasis(String pfID, String date) {
    this.pfID = pfID;
    this.date = date;
  }

  @Override
  public void go(Orchestrator m) {

  }
}
