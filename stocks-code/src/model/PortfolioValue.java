package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import model.StockHandler.StockHandlerBuilder;

public class PortfolioValue {

//  private List<List<String>> stockCountList;
  private String stockCountList;
  private Date date = null;
  private PortfolioValue(String stockCountList, Date date){
    this.stockCountList = stockCountList;
    this.date = date;
  }

  public static PortfolioValueBuilder getBuilder(){
    return new PortfolioValueBuilder();
  }

  public static class PortfolioValueBuilder{
    private String stockCountList;
    private Date date;

    public PortfolioValueBuilder stockCountList(String stockCountList){
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

  List<String> completePortfolioValue(){
    List<String> output = new ArrayList<>();
    String [] lines = stockCountList.split("\n");
    float sum = 0.0F;
    for (String line: lines
    ) {
      String [] nameAndCount = line.split(",");
      float result = stockCountValue(nameAndCount);
      output.add(nameAndCount[0]+":"+String.valueOf(result));
      sum+=result;
    }
    output.add("Total Portfolio Value is : " +String.valueOf(sum));

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
    float stockPrice = Float.parseFloat(stockPriceString.split(":")[1]);
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
