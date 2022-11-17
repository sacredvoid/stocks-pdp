package model.portfolio.filters;

public class DateBeforePredicate {

  private String date;

  public DateBeforePredicate(String date) {
    this.date = date;
  }

  public boolean test(String toCheck) {
    int check = this.date.compareTo(toCheck);
    return check > 0;
  }
}
