package model.dollarcostavg;

/**
 * IDollarCostAvg interface defines the method signatures which can be used to operate on
 * DollarCostAvgStrategy objects.
 */
public interface IDollarCostAvg {

//  Map<String,Float> getCostBasisStockQtyList(Map<String, Float> stockPercentage, double invAmount);
//

  /**
   * dcgStockQtyList() takes in a date and generates the stock quantity list according to the stock
   * percentage strategy.
   *
   * @param date date on which the strategy needs to be applied
   * @return stock quantity list as a string
   */
  String dcgStockQtyList(String date, float actualInvestment);

}
