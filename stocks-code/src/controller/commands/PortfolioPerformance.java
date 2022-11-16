package controller.commands;

import controller.IPortfolioCommands;
import java.io.FileNotFoundException;
import model.Orchestrator;

public class PortfolioPerformance extends APortfolioCommands {

  private String portfolioId;

  private String startDate;

  private String endDate;

  public PortfolioPerformance(String portfolioId, String startDate, String endDate) {
    this.portfolioId = portfolioId;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  @Override
  public void go(Orchestrator m) {
    try {
      setStatusMessage(m.showPerformance(portfolioId, startDate, endDate));
    } catch (FileNotFoundException e) {
      setStatusMessage("No portfolio data found with portfolio ID : " + portfolioId);
    }
  }
}
