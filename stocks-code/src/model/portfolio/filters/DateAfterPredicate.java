package model.portfolio.filters;

public class DateAfterPredicate {

  private String date;

  public DateAfterPredicate(String date) {
    this.date = date;
  }

  public boolean test(String toCheck) {
    int check = this.date.compareTo(toCheck);
    return check >= 0;
  }
}
