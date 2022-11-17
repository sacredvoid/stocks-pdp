package controller.commands;

import java.io.FileNotFoundException;
import model.Orchestrator;

/**
 * The command to display Portfolio performance between two date ranges.
 */
public class PortfolioPerformance extends APortfolioCommands {

  private String portfolioId;

  private String startDate;

  private String endDate;

  /**
   * Instantiates a new Portfolio performance with PFid, start date and end date.
   *
   * @param portfolioId the portfolio id
   * @param startDate   the start date
   * @param endDate     the end date
   */
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
