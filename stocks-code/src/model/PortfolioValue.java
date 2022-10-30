package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import model.StockHandler.StockHandlerBuilder;

/**
 * PortfolioValue class defines methods to perform operations and find the values of the stocks.
 */
public class PortfolioValue {

//  private List<List<String>> stockCountList;
  private String stockCountList;
  private String date = null;
  private PortfolioValue(String stockCountList, String date){
    this.stockCountList = stockCountList;
    this.date = date;
  }

  /**
   * getBuilder() is a static public method create a new PortfolioValueBuilder object.
   * @return PortfolioValueBuilder objecty
   */
  public static PortfolioValueBuilder getBuilder(){
    return new PortfolioValueBuilder();
  }

  /**
   * PortfolioValueBuilder class is a static class which is used to build the object of <p></p>
   * PortfolioValue class.
   */
  public static class PortfolioValueBuilder{
    private String stockCountList;
    private String date;

    /**
     * stockCountList() method is a setter method for the stock count list.
     * @param stockCountList is a list of stock names and quantities.
     * @return the same PortfolioValueBuilder class with stockCountList stored as an attribute.
     */
    public PortfolioValueBuilder stockCountList(String stockCountList){
      this.stockCountList = stockCountList;
      return this;
    }

    /**
     * date() method is a setter method for the date.
     * @param date to retrieve the data of the stock on that specific day.
     * @return the same PortfolioValueBuilder class with date stored as an attribute.
     */
    public PortfolioValueBuilder date(String date){
      this.date = date;
      return this;
    }

    /**
     * build() method of the StockHandlerBuilder class builds a new PortfolioValue object<p></p>
     * with the stockCoundList and date.
     * @return PortfolioValue object.
     */
    public PortfolioValue build(){
      return new PortfolioValue(this.stockCountList,this.date);
    }
  }

  /**
   * completePortfolioValue() method calculates individual and total stock values based on<p></p>
   * the number of each stock.
   * @return a list of individual and total stock values.
   */
  public List<String> completePortfolioValue(){
    List<String> output = new ArrayList<>();
    String [] lines = stockCountList.split("\n");
    float sum = 0.0F;
    for (String line: lines
    ) {
      String [] nameAndCount = line.split(",");
      float result = stockCountValue(nameAndCount);
      output.add(nameAndCount[0]+","+nameAndCount[1]+","+ result);
      sum+=result;
    }
    output.add("Total,-," + sum);

    return output;
  }

  private String stockValueFetcher(String name){
    if( this.date == null){
      return StockHandler.getBuilder()
          .name(name)
          .build()
          .fetchCurrentValue();
    }
    else {
      String nameValue =StockHandler.getBuilder()
          .name(name)
          .date(date)
          .build()
          .fetchByDate();
      if (!nameValue.equals("")) {
        return nameValue;
      }
      else{
        return "";
      }
    }
  }

  private float stockCountValue(String[] stockNameCount){
    String name = stockNameCount[0];
    int count = Integer.parseInt(stockNameCount[1]);
    String stockPriceString = stockValueFetcher(name);
    if(stockPriceString.equals("")){
      return 0;
    }
    float stockPrice = Float.parseFloat(stockPriceString.split(",")[1]);
    return stockPrice*count;
  }

  public static void main(String args[]) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
    Date d = formatter.parse("2022-10-25");
    String data = "GOOG,10\nIBM,20\nTSCO.LON,20\nSHOP.TRT,20\nGPV.TRV,20";

//    List<String> output = PortfolioValue.getBuilder()
//        .stockCountList(data)
//        .date(d)
//        .build()
//        .completePortfolioValue();
//
//    for (String s: output
//    ) {
//      System.out.println(s);
//    }

    List<String> output1 = PortfolioValue.getBuilder()
        .stockCountList(data)
        .build()
        .completePortfolioValue();

    for (String s: output1
    ) {
      System.out.println(s);
    }


  }
}
