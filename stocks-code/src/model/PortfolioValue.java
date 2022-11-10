package model;

import java.util.ArrayList;
import java.util.List;

/**
 * PortfolioValue class defines methods to perform operations and find the values of the stocks.
 */
class PortfolioValue {

  private String stockCountList;
  private String date = null;

  private PortfolioValue(String stockCountList, String date) {
    this.stockCountList = stockCountList;
    this.date = date;
  }

  /**
   * getBuilder() is a static  method create a new PortfolioValueBuilder object.
   *
   * @return PortfolioValueBuilder objecty
   */
  static PortfolioValueBuilder getBuilder() {
    return new PortfolioValueBuilder();
  }

  /**
   * PortfolioValueBuilder class is a static class which is used to build the object of <p></p>
   * PortfolioValue class.
   */
  static class PortfolioValueBuilder {

    private String stockCountList;
    private String date;

    /**
     * stockCountList() method is a setter method for the stock count list.
     *
     * @param stockCountList is a list of stock names and quantities.
     * @return the same PortfolioValueBuilder class with stockCountList stored as an attribute.
     */
    PortfolioValueBuilder stockCountList(String stockCountList) {
      this.stockCountList = stockCountList;
      return this;
    }

    /**
     * date() method is a setter method for the date.
     *
     * @param date to retrieve the data of the stock on that specific day.
     * @return the same PortfolioValueBuilder class with date stored as an attribute.
     */
    PortfolioValueBuilder date(String date) {
      this.date = date;
      return this;
    }

    /**
     * build() method of the StockHandlerBuilder class builds a new PortfolioValue object<p></p>
     * with the stockCoundList and date.
     *
     * @return PortfolioValue object.
     */
    PortfolioValue build() {
      return new PortfolioValue(this.stockCountList, this.date);
    }
  }

  /**
   * completePortfolioValue() method calculates individual and total stock values based on<p></p>
   * the number of each stock.
   *
   * @return a list of individual and total stock values.
   */
  List<String> completePortfolioValue() {
    List<String> output = new ArrayList<>();
    String[] lines = stockCountList.split("\n");
    float sum = 0.0F;
    for (String line : lines
    ) {
      String[] nameAndCount = line.split(",");
      float result = stockCountValue(nameAndCount);
      if (result == 0) {
        output.add(nameAndCount[0] + "," + nameAndCount[1] + "," + "0");
      } else if (result == -1) {
        output.add(nameAndCount[0] + "," + nameAndCount[1] + "," + "API Limit Reached");
      } else if (result == -99) {
        output.add(nameAndCount[0] + "," + nameAndCount[1] + "," + "Invalid Ticker");
      }
      else {
        output.add(String.format(nameAndCount[0] + "," + nameAndCount[1] + ",%.2f", result));
      }
      sum += result;
    }
    output.add(String.format("Total,-,%.2f", sum));

    return output;
  }

  private String stockValueFetcher(String name) {
//    String nameValue = StockHandler.getBuilder()
//        .name(name)
//        .date(date)
//        .build()
//        .fetchByDate();
//    if (!nameValue.equals("")) {
//      return nameValue;
//    } else {
//      return "";
//    }
    return "";
  }

  private float stockCountValue(String[] stockNameCount) {
    String name = stockNameCount[0];
    int count = Integer.parseInt(stockNameCount[1]);
    String stockPriceString = stockValueFetcher(name);
    if (stockPriceString.equals("")) {
      return 0;
    }
    if (stockPriceString.equals("API hit limit reached!!!")) {
      return -1;
    }
    if (stockPriceString.equals("No data found")){
      return -99;
    }
    float stockPrice = Float.parseFloat(stockPriceString.split(",")[1]);
    return stockPrice * count;
  }
}
