package model.dollarcostavg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import model.apistockops.StockHandler;

/**
 * Our data class for Dollar Cost strategy, this stores the Stock, Weight map along with when to
 * invest, how much to invest and start/end dates for the strategy.
 */
public class DollarCostAvgStrategy implements IDollarCostAvg {

  private Map<String, Float> stockPercentMap;
  private float recurrInvAmt;
  private String startDate;
  private String endDate;
  private long recurrCycle;

  /**
   * Instantiates a new Dollar cost avg strategy with the required data from the user.
   *
   * @param stringFloatMap the stock-weightage map for DCA
   * @param recurrInvAmt   the recurrent investment amount
   * @param startDate      the start date of strategy
   * @param endDate        the end date of strategy
   * @param recurrCycle    the recurrent cycle (days)
   */
  public DollarCostAvgStrategy(Map<String, Float> stringFloatMap, float recurrInvAmt,
      String startDate,
      String endDate, long recurrCycle) {
    this.stockPercentMap = stringFloatMap;
    this.recurrInvAmt = recurrInvAmt;
    this.startDate = startDate;
    this.endDate = endDate;
    this.recurrCycle = recurrCycle;
  }

  /**
   * Gets stock percent map.
   *
   * @return the stock percent map
   */
  public Map<String, Float> getStockPercentMap() {
    return stockPercentMap;
  }

  /**
   * Sets stock percent map.
   *
   * @param stockPercentMap the stock percent map
   */
  public void setStockPercentMap(Map<String, Float> stockPercentMap) {
    this.stockPercentMap = stockPercentMap;
  }

  /**
   * Gets recurrent investment amount.
   *
   * @return the recurr inv amt
   */
  public float getRecurrInvAmt() {
    return recurrInvAmt;
  }

  /**
   * Sets recurrent investment amount.
   *
   * @param recurrInvAmt the recurr inv amt
   */
  public void setRecurrInvAmt(float recurrInvAmt) {
    this.recurrInvAmt = recurrInvAmt;
  }

  /**
   * Gets start date.
   *
   * @return the start date
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * Sets start date.
   *
   * @param startDate the start date
   */
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  /**
   * Gets end date.
   *
   * @return the end date
   */
  public String getEndDate() {
    return endDate;
  }

  /**
   * Sets end date.
   *
   * @param endDate the end date
   */
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  /**
   * Gets recurr cycle.
   *
   * @return the recurr cycle
   */
  public long getRecurrCycle() {
    return recurrCycle;
  }

  /**
   * Sets recurr cycle.
   *
   * @param recurrCycle the recurr cycle
   */
  public void setRecurrCycle(long recurrCycle) {
    this.recurrCycle = recurrCycle;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("Recurring Cycle: ").append(this.recurrCycle).append("\n");
    s.append("Recurring Investment Amount: ").append(this.recurrInvAmt).append("\n");
    s.append("Strategy start date: ").append(this.startDate).append("\n");
    s.append("Strategy end date: ").append(this.endDate).append("\n");
    s.append("Stock:Weightage").append("\n");
    String stockWeightMap = this.stockPercentMap.keySet().stream()
        .map(key -> key + ":" + stockPercentMap.get(key))
        .collect(Collectors.joining("\n"));
    s.append(stockWeightMap);
    return s.toString();
  }


  @Override
  public String dcgStockQtyList(String date, float actualInvestment) {

    List<String> stockQtyList = new ArrayList<>();
    if (this.stockPercentMap.size() == 0) {
      return "No stocks were included in this strategy";
    }
    for (Entry<String, Float> stockPercent : this.stockPercentMap.entrySet()
    ) {
      String stockName = stockPercent.getKey();
      float percent = stockPercent.getValue();
      double amtPercent = Math.ceil((actualInvestment * percent) / 100);
      StockHandler sh = StockHandler.getBuilder().build();
      String dcaStatus = sh.DCAHolidayNextWorkingDay(stockName, date);
      if (dcaStatus.contains("change") || dcaStatus.contains("no further")) {
        return dcaStatus;
      }
      float stockPrice = Float.parseFloat(StockHandler.getBuilder()
          .name(stockName)
          .date(date)
          .build()
          .fetchByDate()
          .split(",")[1]);

      float stockQtyPerPercentage = (float) (amtPercent / stockPrice);
      stockQtyList.add(stockName + "," + stockQtyPerPercentage + "," + date + ",BUY");

    }

    return String.join("\n", stockQtyList);
  }
}
