package model;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public abstract class AOrchestrator implements Orchestrator {

  /**
   * Checks if the given date (YYYY-MM-DD) is a weekend and returns true if it is, false otherwise.
   *
   * @param inputDate Date type date in YYYY-MM-DD format
   * @return true/false given if the date is weekend or not
   */
  protected boolean isWeekend(Date inputDate) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(inputDate);
    int day = cal.get(Calendar.DAY_OF_WEEK);
    return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
  }

  private boolean isFuture(Date limit, Date input) {
    return input.after(limit);
  }

  protected boolean isValidDate(String date) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date inputDate = formatter.parse(date);
    LocalDateTime now = LocalDateTime.now();
    Date baseDate = formatter.parse(String.valueOf(now));
    return !(isWeekend(inputDate) || isFuture(baseDate, inputDate));
  }

  /**
   * Shows the existing portfolios in './app_data/PortfolioData' where our application is programmed
   * to store all Portfolios. Also creates the mentioned directory if not present.
   *
   * @return List of string containing the portfolio names/null if no portfolios found
   */

//  @Override
//  public String[] showExistingPortfolios() {
//    File f = new File("." + osSep + "app_data" + osSep + PORTFOLIO_DATA_PATH);
//    if (!f.isDirectory()) {
//      f.mkdirs();
//    }
//    String[] filesList = f.list();
//    if (filesList.length == 0) {
//      return null;
//    } else {
//      return filesList;
//    }
//  }

  /**
   * generate a 6 digit random portfolio number for the application.
   *
   * @return 6-digit long string
   */
  @Override
  public String generatePortfolioID() {
    int leftLimit = 48; // letter 'a'
    int rightLimit = 57; // letter 'z'
    int targetStringLength = 6;
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

}
