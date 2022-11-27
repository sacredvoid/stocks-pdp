package model.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
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
  private boolean isWeekend(Date inputDate) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(inputDate);
    int day = cal.get(Calendar.DAY_OF_WEEK);
    return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
  }

  private boolean isFuture(Date limit, Date input) {
    return input.after(limit);
  }

  @Override
  public boolean checkData(String date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date inputDate;
    LocalDateTime now;
    Date baseDate;
    try {
      inputDate = formatter.parse(date);
      now = LocalDateTime.now();
      baseDate = formatter.parse(String.valueOf(now));
    }
    catch (ParseException e) {
      return false;
    }
    return !(isWeekend(inputDate) || isFuture(baseDate, inputDate)) ;
  }

//  private boolean checkDateFormat(String date) {
//    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd",Locale.US).withResolverStyle(
//        ResolverStyle.STRICT);
//
//    try {
//      dateTimeFormatter.parse(date);
//    }
//    catch (DateTimeException e) {
//      return false;
//    }
//    return true;
//  }


}
