package model.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * DateValidator class defines methods to check and validate dates.
 */
public class DateValidator implements IDataValidator {

  /**
   * Checks if the given date (YYYY-MM-DD) is a weekend and returns true if it is, false otherwise.
   *
   * @param inputDate Date type date in YYYY-MM-DD format
   * @return true/false given if the date is weekend or not
   */
  private static boolean isWeekend(Date inputDate) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(inputDate);
    int day = cal.get(Calendar.DAY_OF_WEEK);
    return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
  }

  private static boolean isFuture(Date limit, Date input) {
    return input.after(limit);
  }

  public boolean checkData(String date) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date inputDate = formatter.parse(date);
    LocalDateTime now = LocalDateTime.now();
    Date baseDate = formatter.parse(String.valueOf(now));
    return !(isWeekend(inputDate) || isFuture(baseDate, inputDate));
  }

}
