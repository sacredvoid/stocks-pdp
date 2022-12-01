package model.dollarcostavg;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class DcaStrategyToCSV {

  public static String getAllTransactions(DollarCostAvgStrategy strategy, String tempDate,
      long recurr, float commission, String finalTransactions)
      throws ParseException {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    System.out.println(tempDate);
    tempDate=tempDate.strip();
    LocalDate lTempDate = LocalDate.parse(tempDate,df);
    LocalDate lStartDate = LocalDate.parse(strategy.getStartDate(), df);
    LocalDate lEndDate;
    if(strategy.getEndDate().isEmpty()){
      lEndDate = LocalDate.now();
    }
    else{
      lEndDate = LocalDate.parse(strategy.getEndDate(),df);
    }
//    LocalDate lEndDate = LocalDate.parse(strategy.getEndDate(), df);

    if(lTempDate.isBefore(lStartDate)){
      return finalTransactions;
    }
    if(lTempDate.isAfter(lEndDate)){
      return finalTransactions;
    }

    LocalDate nonWeekendDate;

    switch (DayOfWeek.of(lTempDate.get(ChronoField.DAY_OF_WEEK))) {
      case SATURDAY:
        nonWeekendDate = lTempDate.plusDays(2);
        break;
      case SUNDAY:
        nonWeekendDate = lTempDate.plusDays(1);
        break;
      default:
        nonWeekendDate = lTempDate;
    }
    String transactionList = strategy.dcgStockQtyList(nonWeekendDate.toString(),
        strategy.getRecurrInvAmt()-
            (commission*strategy.getStockPercentMap().size()));
    if(transactionList.contains("change")){
      return getAllTransactions(strategy,transactionList.split(" ")[3],recurr,commission,finalTransactions);
    }
    else if(transactionList.contains("no further")){
      return finalTransactions;
    }

    LocalDate nextDate = lTempDate.plusDays(recurr);
    return getAllTransactions(strategy,nextDate.toString(),recurr,commission,
        finalTransactions+transactionList+"\n");

  }

}
