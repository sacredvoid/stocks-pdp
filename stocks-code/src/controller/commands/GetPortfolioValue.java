package controller.commands;

import controller.IPortfolioCommands;
import model.ModelOrchestrator;
import model.Orchestrator;

public class GetPortfolioValue extends APortfolioCommands {

  private String pfID;
  private String date;

  public GetPortfolioValue(String pfID, String date) {
    this.pfID = pfID;
    this.date = date;
  }
  @Override
  public void go(Orchestrator m) {

  }
}
