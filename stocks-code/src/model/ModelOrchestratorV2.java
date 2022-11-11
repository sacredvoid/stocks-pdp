package model;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import model.fileops.FileOps;
import model.fileops.JSONFileOps;
import model.portfolio.CSVToPortfolioAdapter;
import model.portfolio.PortfolioData;
import model.portfolio.PortfolioDataAdapter;
import model.portfolio.PortfolioToCSVAdapter;
import model.portfolio.Utility;
import model.portfolio.filters.FilterPortfolio;


public class ModelOrchestratorV2 extends AOrchestrator {

  private FileOps jsonParser = new JSONFileOps();

  @Override
  public String getPortfolio(String portfolioID) throws FileNotFoundException {
    String pfData = jsonParser.readFile(portfolioID + ".json", PORTFOLIO_DATA_PATH);
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);
    String latestDate = Utility.getLatestDate(parsedPFData);
    String csvPortfolio = PortfolioToCSVAdapter.buildStockQuantityList(
        parsedPFData.get(latestDate).getStockList()
    );
    return csvPortfolio;
  }

  @Override
  public String createPortfolio(String portfolioData) {
    String newPFID = this.generatePortfolioID();
    try {
      Map<String, PortfolioData> translated = CSVToPortfolioAdapter.buildPortfolioData(
          portfolioData);
      jsonParser.writeToFile(newPFID + ".json", PORTFOLIO_DATA_PATH, new Gson().toJson(translated));
    } catch (IOException io) {
      return "Failed to create portfolio";
    }
    return "Created Portfolio with ID: " + newPFID;
  }

  @Override
  public String getPortfolioValue(String date, String data) throws ParseException {
    return null;
  }

  @Override
  public String getPortfolioValueByID(String date, String pfID) throws FileNotFoundException {
    String pfData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);
    Map<String, PortfolioData> filteredData = FilterPortfolio.getPortfolioAfterDate(parsedPFData,date);
//    System.out.println();
    return filteredData.toString();
  }

  @Override
  public String loadExternalPortfolio(String path) throws FileNotFoundException {
    return null;
  }


}
