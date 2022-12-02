package model.portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import model.portfolio.filters.FilterPortfolio;

/**
 * The class that handles buying and selling in the past. Essentially, if a user buys in the past,
 * we correctly append the stock data on that date (fetching the previous list of stocks) and then
 * appending this new list to any future entries in the portfolio. Similar operation for selling.
 */
public class CascadeTransactions {

  /**
   * Update portfolio map.
   *
   * @param operation        the operation, buy or sell
   * @param currentPF        the current portfolio map
   * @param newStockToAdd    the new stock to add
   * @param givenDate        the given date to perform transaction on
   * @param totalTransaction the total transaction value (buy/sell)
   * @param totalCommission  the total commission
   * @return the map
   */
  public static <T extends PortfolioData> Map<String, T> updatePortfolio(
      String operation,
      Map<String, T> currentPF,
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
        currentPF.get(givenDate)
            .setTotalInvested(currentPF.get(givenDate).getTotalInvested() + totalTransaction);
        currentPF.get(givenDate)
            .setTotalCommission(currentPF.get(givenDate).getTotalCommission() + totalCommission);
        currentPF.get(givenDate).setTotalEarned(currentPF.get(givenDate).getTotalEarned());
        cascade(currentPF, newStockToAdd, givenDate, "buy", totalTransaction, totalCommission);
        // cascade
      } else {
        PortfolioData f = getMostRecentStockList(currentPF, givenDate);
        List<StockData> tempList = deepCopy(f.getStockList());
        T tempP = (T) new PortfolioData(tempList, totalTransaction + f.getTotalInvested(),
            totalCommission + f.getTotalCommission(), f.getTotalEarned());
        tempP.addStock(newStockToAdd);
        currentPF.put(givenDate, tempP);
        // cascade
        cascade(currentPF, newStockToAdd, givenDate, "buy", totalTransaction, totalCommission);
      }
    } else {
      if (currentPF.containsKey(givenDate)) {
        // subtract new stock
        String newStockName = newStockToAdd.getStockName();
        List<StockData> theDayStockList = currentPF.get(givenDate).getStockList();
        for (StockData s : theDayStockList
        ) {
          if (s.getStockName().equals(newStockName)) {
            if (s.getQuantity() >= newStockToAdd.getQuantity()) {
              s.setQuantity(s.getQuantity() - newStockToAdd.getQuantity());
              currentPF.get(givenDate)
                  .setTotalInvested(currentPF.get(givenDate).getTotalInvested());
              currentPF.get(givenDate).setTotalCommission(
                  currentPF.get(givenDate).getTotalCommission() + totalCommission);
              currentPF.get(givenDate)
                  .setTotalEarned(currentPF.get(givenDate).getTotalEarned() + totalTransaction);
            }
          }
        }
        // cascade
        cascade(currentPF, newStockToAdd, givenDate, "sell", totalTransaction, totalCommission);
      } else {
        PortfolioData f = getMostRecentStockList(currentPF, givenDate);
        List<StockData> tempList = deepCopy(f.getStockList());
        T tempP = (T) new PortfolioData(tempList, f.getTotalInvested(),
            totalCommission + f.getTotalCommission(), totalTransaction + f.getTotalEarned());
        if (tempP.getQuantity(newStockToAdd.getStockName()) >= newStockToAdd.getQuantity()) {
          tempP.removeStock(newStockToAdd);
        }
        currentPF.put(givenDate, tempP);
        // cascade
        cascade(currentPF, newStockToAdd, givenDate, "sell", totalTransaction, totalCommission);
      }

    }
    return currentPF;
  }

  private static List<StockData> deepCopy(List<StockData> oldStockList) {
    List<StockData> newList = new ArrayList<>();
    for (StockData s : oldStockList
    ) {
      newList.add(new StockData(s.getStockName(), s.getQuantity()));
    }
    return newList;
  }

  private static <T extends PortfolioData> PortfolioData getMostRecentStockList(
      Map<String, T> currentPF,
      String givenDate) {
    List<StockData> tempList = new ArrayList<>();
    Map<String, T> filteredBeforeDateMap =
        FilterPortfolio.getPortfolioBeforeDate(currentPF, givenDate);
    T lastPFData = null;
    if (filteredBeforeDateMap.size() > 0) {
      String mostRecentPFBeforeDate = Utility.getLatestDate(filteredBeforeDateMap);
      lastPFData = filteredBeforeDateMap.get(mostRecentPFBeforeDate);
      tempList = deepCopy(lastPFData.getStockList());
    }
    if (lastPFData == null) {
      return new PortfolioData(new ArrayList<>(), 0, 0, 0);
    }
    lastPFData.setStockList(tempList);
    return lastPFData;
  }

  /**
   * Cascade is basically carrying up the new list of stock data and updating to any future entries
   * in the portfolio.
   *
   * @param currentPF        the current pf
   * @param newStock         the new stock
   * @param date             the date
   * @param type             the type
   * @param totalTransaction the total transaction
   * @param totalCommission  the total commission
   */
  public static <T extends PortfolioData> void cascade(
      Map<String, T> currentPF,
      StockData newStock,
      String date,
      String type,
      float totalTransaction,
      float totalCommission
  ) {
    Map<String, T> filteredAfterDateMap =
        FilterPortfolio.getPortfolioAfterDate(currentPF, date);
    if (filteredAfterDateMap.size() > 0) {
      for (Entry<String, T> pfEntry : currentPF.entrySet()) {
        if (filteredAfterDateMap.containsKey(pfEntry.getKey())) {
          if (type.equals("buy")) {
            pfEntry.getValue().addStock(newStock);
            pfEntry.getValue()
                .setTotalInvested(pfEntry.getValue().getTotalInvested() + totalTransaction);
            pfEntry.getValue()
                .setTotalCommission(pfEntry.getValue().getTotalCommission() + totalCommission);
            pfEntry.getValue().setTotalEarned(pfEntry.getValue().getTotalEarned());
          } else {
            if (newStock.getQuantity() <= pfEntry.getValue().getQuantity(newStock.getStockName())) {
              pfEntry.getValue().removeStock(newStock);
              pfEntry.getValue()
                  .setTotalEarned(pfEntry.getValue().getTotalEarned() + totalTransaction);
              pfEntry.getValue()
                  .setTotalCommission(pfEntry.getValue().getTotalCommission() + totalCommission);
              pfEntry.getValue().setTotalInvested(pfEntry.getValue().getTotalInvested());
            }
          }
        }
      }
    }
  }
}