package model;

import java.io.FileNotFoundException;
import java.text.ParseException;

public class ModelOrchestratorV2  extends AOrchestrator{

  @Override
  public String getPortfolio(String portfolioID) throws FileNotFoundException {
    return null;
  }

  @Override
  public String createPortfolio(String portfolioData) {
    return null;
  }

  @Override
  public String getPortfolioValue(String date, String data) throws ParseException {
    return null;
  }

  @Override
  public String[] showExistingPortfolios() {
    return new String[0];
  }

  @Override
  public String loadExternalPortfolio(String path) throws FileNotFoundException {
    return null;
  }
}
