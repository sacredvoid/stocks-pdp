package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

  public static Date getDate(String date) throws ParseException {
    return new SimpleDateFormat("yyyy-MM-dd").parse(date);
  }
}
