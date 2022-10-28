package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import model.StockHandler.StockHandlerBuilder;

public class PortfolioValue {

  private List<List<String>> stockCountList;
  private Date date = null;
  private PortfolioValue(List<List<String>> stockCountList, Date date){
    this.stockCountList = stockCountList;
    this.date = date;
  }

  public static PortfolioValueBuilder getBuilder(){
    return new PortfolioValueBuilder();
  }

  public static class PortfolioValueBuilder{
    private List<List<String>> stockCountList;
    private Date date;

    public PortfolioValueBuilder stockCountList(List<List<String>> stockCountList){
      this.stockCountList = stockCountList;
      return this;
    }

    public PortfolioValueBuilder date(Date date){
      this.date = date;
      return this;
    }

    public PortfolioValue build(){
      return new PortfolioValue(this.stockCountList,this.date);
    }
  }

  String totalPortfolioValue(){
    float sum = 0;
//    StockHandlerBuilder stockPriceListBuilder = StockHandler.getBuilder();
    for(List<String> l: this.stockCountList){
//      String name = l.get(0);
//      int count = Integer.parseInt(l.get(1));
//      String stockPriceList = stockValueFetcher(name);
//      int stockPrice = Integer.parseInt(stockPriceList.split(":")[1]);
//      sum += stockPrice*count;
      sum += stockCountValue(l);
    }
    return String.valueOf(sum);
  }


  List<List<String>> allStocksValue(){
    List<List<String>> sepStockValueList = new ArrayList<List<String>>();
    for(List<String> l: this.stockCountList){
//      String name = l.get(0);
//      int count = Integer.parseInt(l.get(1));
//      String stockPriceList = stockValueFetcher(name);
//      int stockPrice = Integer.parseInt(stockPriceList.split(":")[1]);
      List<String> tempList= new ArrayList<String>(2);
      tempList.add(l.get(0));
      tempList.add(String.valueOf(stockCountValue(l)));
      sepStockValueList.add(tempList);
    }

    return sepStockValueList;
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

  private float stockCountValue(List<String> stockNameCount){
    String name = stockNameCount.get(0);
    int count = Integer.parseInt(stockNameCount.get(1));
    String stockPriceString = stockValueFetcher(name);
    if(stockPriceString.equals("")){
      return 0;
    }
    float stockPrice = Float.parseFloat(stockPriceString.split(":")[1]);
    return stockPrice*count;
  }

  public static void main(String args[]) throws ParseException {
    List<List<String>> tempList = new ArrayList<List<String>>(2);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
    Date d = formatter.parse("2022-10-25");
    List<String> l = new ArrayList<String>(2);
    l.add("GOOG");
    l.add("10");
    tempList.add(l);

    l = new ArrayList<String>(2);
    l.add("IBM");
    l.add("20");
    tempList.add(l);

    l = new ArrayList<String>(2);
    l.add("TSCO.LON");
    l.add("20");
    tempList.add(l);

    l = new ArrayList<String>(2);
    l.add("SHOP.TRT");
    l.add("20");
    tempList.add(l);

    l = new ArrayList<String>(2);
    l.add("GPV.TRV");
    l.add("20");
    tempList.add(l);

    l = new ArrayList<String>(2);
    l.add("DAI.DEX");
    l.add("20");
    tempList.add(l);



    List<List<String>> sepSVL ;

    System.out.println("Finding the portfolio values when the date is given\n");
    PortfolioValue pvb1 = PortfolioValue.getBuilder()
        .stockCountList(tempList)
        .date(d)
        .build();

    System.out.println("Total portfolio Value : "+pvb1.totalPortfolioValue());
    sepSVL = pvb1.allStocksValue();

    System.out.println("Separate Stock total Value List");
    for(List<String> svl: sepSVL){
      System.out.println(svl.get(0)+ ":" + svl.get(1));
    }

    System.out.println("Finding the current portfolio values\n");
    PortfolioValue pvb2 = PortfolioValue.getBuilder()
        .stockCountList(tempList)
        .build();

    System.out.println("Total portfolio Value : "+pvb2.totalPortfolioValue());

    sepSVL = pvb2.allStocksValue();

    System.out.println("Separate Stock total Value List");
    for(List<String> svl: sepSVL){
      System.out.println(svl.get(0)+ ":" + svl.get(1));
    }

  }
}
