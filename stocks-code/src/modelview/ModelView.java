package modelview;

import model.Orchestrator;

public class ModelView implements IModelView{

  private Orchestrator morch;

  public ModelView(Orchestrator modelOrchestrator) {
    this.morch = modelOrchestrator;
  }

  @Override
  public String getPortfolio(String portfolioID) {
    return null;
  }

  @Override
  public String getPortfolioValue(String date, String portfolioID) {
    return null;
  }
}
