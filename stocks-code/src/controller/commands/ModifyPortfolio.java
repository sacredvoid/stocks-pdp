package controller.commands;

import model.Orchestrator;

public class ModifyPortfolio extends APortfolioCommands {

  private String portfolioID;
  private String data;

  public ModifyPortfolio(String portfolioID, String data) {
    this.portfolioID = portfolioID;
    this.data = data;
  }

  @Override
  public void go(Orchestrator m) {
    setStatusMessage(m.editExistingPortfolio(this.portfolioID,this.data));
  }
}
