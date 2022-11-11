package model.portfolio.filters;

public class DatePredicate {

  private String date;

  public DatePredicate(String date) {
    this.date = date;
  }

  public boolean test(String toCheck) {
    int check = this.date.compareTo(toCheck);
    return check >= 0;
  }
}
