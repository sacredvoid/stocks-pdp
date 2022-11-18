package controller.commands;

import model.Orchestrator;

/**
 * The command to Modify portfolio given user's decisions (buy/sell).
 */
public class ModifyPortfolio extends APortfolioCommands {

  private String portfolioID;
  private String data;

  /**
   * Instantiates a new Modify portfolio with PFid and data from user which contains stock,quantity,
   * date, call (buy/sell).
   *
   * @param portfolioID the portfolio id
   * @param data        the data
   */
  public ModifyPortfolio(String portfolioID, String data) {
    this.portfolioID = portfolioID;
    this.data = data;
  }

  @Override
  public void runCommand(Orchestrator m) {
    if (this.portfolioID.isEmpty() || this.data.isEmpty()) {
      setStatusMessage("");
    }
    setStatusMessage(m.editExistingPortfolio(this.portfolioID, this.data));
  }
}
