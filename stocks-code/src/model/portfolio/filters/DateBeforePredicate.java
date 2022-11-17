package model.portfolio.filters;

/**
 * The Date before predicate which is used to find dates before the given date.
 */
public class DateBeforePredicate {

  private String date;

  /**
   * Instantiates a new Date before predicate with a base date to check other dates with.
   *
   * @param date the date
   */
  public DateBeforePredicate(String date) {
    this.date = date;
  }

  /**
   * Test boolean class which returns true if given date is before(past) the base date.
   *
   * @param toCheck the to check
   * @return the boolean
   */
  public boolean test(String toCheck) {
    int check = this.date.compareTo(toCheck);
    return check > 0;
  }
}
