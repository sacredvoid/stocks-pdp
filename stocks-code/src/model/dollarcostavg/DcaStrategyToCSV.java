package model.dollarcostavg;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

/**
 * The type Dca strategy to csv.
 */
public class DcaStrategyToCSV {

  /**
   * Gets all transactions to be executed given a DCA strategy. Since all the operations in DCA are
   * buy, we collect them and send them together to our buy method. This method returns a CSV string
   * containing multiple lines of "Stock Name, Quantity, Date, BUY".
   *
   * @param strategy          the strategy from user
   * @param tempDate          the temp date
   * @param recur             the recurrent cycle
   * @param commission        the commission
   * @param finalTransactions the final transactions
   * @return all the transactions to be executed given a strategy
   * @throws ParseException the parse exception
   */
  public static String getAllTransactions(DollarCostAvgStrategy strategy, String tempDate,
      long recur, float commission, String finalTransactions)
      throws ParseException {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    System.out.println(tempDate);
    tempDate = tempDate.strip();
    LocalDate lTempDate = LocalDate.parse(tempDate, df);
    LocalDate lStartDate = LocalDate.parse(strategy.getStartDate(), df);
    LocalDate lEndDate;
    if (strategy.getEndDate().isEmpty()) {
      lEndDate = LocalDate.now();
    } else {
      lEndDate = LocalDate.parse(strategy.getEndDate(), df);
    }
//    LocalDate lEndDate = LocalDate.parse(strategy.getEndDate(), df);

    if (lTempDate.isBefore(lStartDate)) {
      return finalTransactions;
    }
    if (lTempDate.isAfter(lEndDate)) {
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
        strategy.getRecurrInvAmt() -
            (commission * strategy.getStockPercentMap().size()));
    if (transactionList.contains("change")) {
      return getAllTransactions(strategy, transactionList.split(" ")[3], recur, commission,
          finalTransactions);
    } else if (transactionList.contains("no further")) {
      return finalTransactions;
    }

    LocalDate nextDate = lTempDate.plusDays(recur);
    return getAllTransactions(strategy, nextDate.toString(), recur, commission,
        finalTransactions + transactionList + "\n");

  }

}
