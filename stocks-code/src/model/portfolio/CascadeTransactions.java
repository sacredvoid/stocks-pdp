package model.portfolio;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.fileops.FileOps;
import model.fileops.JSONFileOps;
import model.portfolio.filters.FilterPortfolio;

public class CascadeTransactions {

  public static Map<String,PortfolioData> updatePortfolio(String operation,
      Map<String, PortfolioData> currentPF, StockData newStocks, String date,
      float totalTransaction, float totalCommission) {

    Map<String, PortfolioData> filteredAfterDateMap = FilterPortfolio.getPortfolioAfterDate(
        currentPF, date);

    Map<String, PortfolioData> filteredBeforeDateMap = FilterPortfolio.getPortfolioBeforeDate(
        currentPF, date);

    String mostRecentPastDate = Utility.getLatestDate(filteredBeforeDateMap);

    if (operation.equals("BUY")) {
      if (currentPF.containsKey(date)) {
        if(filteredAfterDateMap.size()!=0){
        filteredAfterDateMap.replaceAll((pfDate, newPFData) ->
        {
          newPFData.addStock(newStocks);
          newPFData.setTotalInvested(currentPF.get(date).getTotalInvested() + totalTransaction);
          newPFData.setTotalCommission(currentPF.get(date).getTotalCommission() + totalCommission);
          return newPFData;
        });}
        else{
          if(currentPF.get(date).getStockList().contains(newStocks)){

          }
          else{
            currentPF.get(date).addStock(newStocks);
            currentPF.get(date).setTotalInvested(currentPF.get(date).getTotalInvested() + totalTransaction);
            currentPF.get(date).setTotalCommission(currentPF.get(date).getTotalCommission() + totalCommission);
          }
          return currentPF;
        }
      }
      // Append it to the list
      else {
        List<StockData> tempList = new ArrayList<>();
        tempList.add(newStocks);
        if(!filteredBeforeDateMap.isEmpty()) {
          for (StockData pastData : filteredBeforeDateMap.get(mostRecentPastDate).getStockList()) {
            tempList.add(pastData);
          }
        }
        PortfolioData tempP = new PortfolioData(tempList, totalTransaction, totalCommission, 0);
        currentPF.put(date, tempP);
        updatePortfolio(operation,currentPF,newStocks,date,totalTransaction,totalCommission);
      }
      if (operation.equals("SELL")) {

      }
      currentPF.putAll(filteredAfterDateMap);
    }
    return currentPF;
  }

//  private void pullLastPortfolioData(Map<String, PortfolioData> current)


  public static void main(String[] args) throws FileNotFoundException {
    FileOps jsonParser = new JSONFileOps();
    String pfData = jsonParser.readFile("1234.json", ".\\PortfolioData");
    Map<String, PortfolioData> parsedPFData = PortfolioDataAdapter.getObject(pfData);
    String date = "2021-10-02";
    Map<String, PortfolioData> submap = FilterPortfolio.getPortfolioAfterDate(parsedPFData,date);
    Map<String, PortfolioData> filteredBeforeDateMap = FilterPortfolio.getPortfolioBeforeDate(parsedPFData, date);

    System.out.println(submap);
    System.out.println(filteredBeforeDateMap);
//    if()
    filteredBeforeDateMap.get("2020-10-11").addStock(new StockData("AAPL",20));
    System.out.println(filteredBeforeDateMap);
  }

}
