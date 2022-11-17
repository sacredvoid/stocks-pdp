package controller.commands;

import model.Orchestrator;

/**
 * The command for Create portfolio which calls the respective model object method to create a new
 * user portfolio with given data.
 */
public class CreatePortfolio extends APortfolioCommands {

  private String stockData;

  /**
   * Instantiates a new Create portfolio and sets the user data.
   *
   * @param data the stock data from user input
   */
  public CreatePortfolio(String data) {
    this.stockData = data;
  }

  @Override
  public void go(Orchestrator m) {
    setStatusMessage(m.createPortfolio(this.stockData));
  }
}
