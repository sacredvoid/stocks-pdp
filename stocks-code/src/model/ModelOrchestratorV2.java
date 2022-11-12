package model;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import model.fileops.FileOps;
import model.fileops.JSONFileOps;
import model.portfolio.CSVToPortfolioAdapter;
import model.portfolio.PortfolioData;
import model.portfolio.PortfolioDataAdapter;
import model.portfolio.PortfolioToCSVAdapter;
import model.portfolio.StockData;
import model.portfolio.Utility;


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
  public String getPortfolioValue(String date, String pfId) throws ParseException {
    String stockCountList;
    try{
      stockCountList = this.getPortfolioValueByID(date,pfId);
    }catch( FileNotFoundException e){
      return "Sorry, No stocks for given date";
    }
    if(stockCountList.equals("Sorry, no data for given date.")){
      return stockCountList;
    } else if(stockCountList.equals("Sorry, no stocks for given date.")){
      return stockCountList;
    }
    List<String> portfolioValue = PortfolioValue.getBuilder()
        .stockCountList(stockCountList)
        .date(date)
        .build()
        .completePortfolioValue();

    String portfolioValueCsvFormat = String.join("\n",portfolioValue);
//    System.out.println(portfolioValueCsvFormat);
//    return null;
    return portfolioValueCsvFormat;
  }

  @Override
  public String getPortfolioValueByID(String date, String pfID) throws FileNotFoundException {
    String pfData = jsonParser.readFile(pfID + ".json", PORTFOLIO_DATA_PATH);
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);

//    // Example for filtering data
//    Map<String, PortfolioData> filteredData = FilterPortfolio.getPortfolioAfterDate(parsedPFData,date);
    // Send this stockDataCSV to Aakash
    List<StockData> stockDataForDate;
    try {
      stockDataForDate = parsedPFData.getOrDefault(date,null).getStockList();
    }
    catch (NullPointerException n) {
      return "Sorry, no data for given date.";
    }
    if(stockDataForDate != null) {
      String stockDataCSV = PortfolioToCSVAdapter.buildStockQuantityList(stockDataForDate);
      return stockDataCSV;
    }
    else return "Sorry, no stocks for given date.";
    //    System.out.println();
  }

  @Override
  public String loadExternalPortfolio(String path) throws FileNotFoundException {
    return null;
  }


}
