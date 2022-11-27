package model.portfolio;

import java.util.List;
import model.ModelOrchestratorV2;
import model.costbasis.CostBasisStrategy;

public class CostBasisPortfolioData extends PortfolioData{

  private CostBasisStrategy costBasisStrategy;

  /**
   * PortfolioData() constructor takes in the stock data list, total invested and total commission for
   * a particular record creates a new PortfolioData object.
   *
   * @param sd              a list of stock data
   * @param totalInvested   the total amount invested by the user
   * @param totalCommission the total amount of commission taken by the application
   * @param totalEarned
   */
  public CostBasisPortfolioData(List<StockData> sd, float totalInvested,
      float totalCommission, float totalEarned, CostBasisStrategy costBasisStrategy ) {
    super(sd, totalInvested, totalCommission, totalEarned);
    this.costBasisStrategy =costBasisStrategy;
  }

  public CostBasisStrategy getCostBasisStrategy() {
    return costBasisStrategy;
  }

  public void setCostBasisStrategy(CostBasisStrategy costBasisStrategy) {
    this.costBasisStrategy = costBasisStrategy;
  }

  public static void main(String args[]){

  }
}
