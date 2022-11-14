package modelview;

import java.io.FileNotFoundException;
import model.Orchestrator;

public class ModelView implements IModelView{

  private Orchestrator morch;

  public ModelView(Orchestrator modelOrchestrator) {
    this.morch = modelOrchestrator;
  }

  @Override
  public String getLatestPortfolioComposition(String portfolioID) {
    return null;
  }

  @Override
  public String getPortfolioValue(String date, String data) {
    return null;
  }

  @Override
  public String[] getExistingPortfolios() {
    return this.morch.getExistingPortfolios();
  }

  @Override
  public String getPortfolioCompositionByDate(String date, String pfID) throws FileNotFoundException {
    return this.morch.getPortfolioCompositionByDate(date,pfID);
  }
}
