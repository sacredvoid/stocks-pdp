package model.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * DateValidator class defines methods to check and validate dates.
 */
public class DateValidator implements IDataValidator {

  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

  /**
   * Checks if the given date (YYYY-MM-DD) is a weekend and returns true if it is, false otherwise.
   *
   * @param inputDate Date type date in YYYY-MM-DD format
   * @return true/false given if the date is weekend or not
   */
  private boolean isWeekend(Date inputDate) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(inputDate);
    int day = cal.get(Calendar.DAY_OF_WEEK);
    return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
  }

  private boolean isFuture(Date input) {
    LocalDateTime now = LocalDateTime.now();
    Date baseDate;
    try {
      baseDate = formatter.parse(String.valueOf(now));
    }
    catch (ParseException e) {
      return false;
    }
    return input.after(baseDate);
  }

  @Override
  public boolean checkData(String date) {

    Date inputDate;
    try {
      inputDate = formatter.parse(date);
    }
    catch (ParseException e) {
      return false;
    }
    return !(isWeekend(inputDate) || isFuture(inputDate)) ;
  }

  public static boolean checkDateFormat (String date) {
    try {
      formatter.parse(date);
    }
    catch (ParseException e) {
      return false;
    }
    return true;
  }


}
