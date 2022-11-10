package model;

import java.io.FileNotFoundException;
import java.text.ParseException;
import model.fileops.FileOps;
import model.fileops.JSONFileOps;

public class ModelOrchestratorV2 extends AOrchestrator{

  private FileOps jsonParser;

  @Override
  public String getPortfolio(String portfolioID) throws FileNotFoundException {
    String pfData; // How to get PortfolioDataMapper here????????
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
  public String loadExternalPortfolio(String path) throws FileNotFoundException {
    return null;
  }
}
