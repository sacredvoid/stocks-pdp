package model.portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import model.portfolio.filters.FilterPortfolio;

public class CascadeV2 {

  public static Map<String, PortfolioData> updatePortfolio
      (
          String operation,
          Map<String, PortfolioData> currentPF,
          StockData newStockToAdd,
          String givenDate,
          float totalTransaction,
          float totalCommission
      ) {

    if (operation.equals("BUY")) {
      // Get the last set of stocks
      if (currentPF.containsKey(givenDate)) {
        // add new stock
        currentPF.get(givenDate).addStock(newStockToAdd);
        cascade(currentPF,newStockToAdd,givenDate,"buy");
        // cascade
      } else {
        List<StockData> tempList = new ArrayList<StockData>();
        Map<String, PortfolioData> filteredBeforeDateMap =
            FilterPortfolio.getPortfolioBeforeDate(currentPF, givenDate);
        if(filteredBeforeDateMap.size() > 0) {
          String mostRecentPFBeforeDate = Utility.getLatestDate(filteredBeforeDateMap);
          PortfolioData lastPFData = filteredBeforeDateMap.get(mostRecentPFBeforeDate);
          List<StockData> oldStockList = lastPFData.getStockList();
          for (StockData s: oldStockList
          ) {
            StockData sCopy = new StockData(s.getStockName(), s.getQuantity());
            tempList.add(sCopy);
          }
        }
//        tempList.add(newStockToAdd);
        PortfolioData tempP = new PortfolioData(tempList, totalTransaction, totalCommission, 0);
        tempP.addStock(newStockToAdd);
        currentPF.put(givenDate, tempP);
        // cascade
        cascade(currentPF,newStockToAdd,givenDate,"buy");
      }
    }
    else {
        if (currentPF.containsKey(givenDate)) {
          // subtract new stock
          String newStockName = newStockToAdd.getStockName();
          List<StockData> theDayStockList = currentPF.get(givenDate).getStockList();
          for (StockData s: theDayStockList
          ) {
            if(s.getStockName().equals(newStockName)) {
              if(s.getQuantity() >= newStockToAdd.getQuantity()) {
                s.setQuantity(s.getQuantity() - newStockToAdd.getQuantity());
              }
            }
          }
          // cascade
          cascade(currentPF,newStockToAdd,givenDate,"sell");
        } else {
          List<StockData> tempList = new ArrayList<StockData>();
          Map<String, PortfolioData> filteredBeforeDateMap =
              FilterPortfolio.getPortfolioBeforeDate(currentPF, givenDate);
          if(filteredBeforeDateMap.size() > 0) {
            String mostRecentPFBeforeDate = Utility.getLatestDate(filteredBeforeDateMap);
            PortfolioData lastPFData = filteredBeforeDateMap.get(mostRecentPFBeforeDate);
            List<StockData> oldStockList = lastPFData.getStockList();
            for (StockData s: oldStockList
            ) {
              StockData sCopy = new StockData(s.getStockName(), s.getQuantity());
              tempList.add(sCopy);
            }
          }
          PortfolioData tempP = new PortfolioData(tempList, totalTransaction, totalCommission, 0);
          if(tempP.getQuantity(newStockToAdd.getStockName()) >= newStockToAdd.getQuantity()){
            tempP.removeStock(newStockToAdd);
          }
          currentPF.put(givenDate, tempP);
          // cascade
          cascade(currentPF,newStockToAdd,givenDate,"sell");
        }

    }
    return currentPF;
  }

  public static void cascade(
      Map<String, PortfolioData> currentPF,
      StockData newStock,
      String date,
      String type
  ) {
    Map<String, PortfolioData> filteredAfterDateMap =
        FilterPortfolio.getPortfolioAfterDate(currentPF, date);
    if(filteredAfterDateMap.size() > 0) {
        for (Entry<String, PortfolioData> pfEntry : currentPF.entrySet()) {
          if (filteredAfterDateMap.containsKey(pfEntry.getKey())) {
            if(type.equals("buy")){
              pfEntry.getValue().addStock(newStock);
            }
            else {
              if(newStock.getQuantity() <= pfEntry.getValue().getQuantity(newStock.getStockName())){
                pfEntry.getValue().removeStock(newStock);
              }
            }
          }
        }
      }
    }
}