package model.portfolio.filters;

/**
 * The Date after predicate which is used to find dates after the given date.
 */
public class DateAfterPredicate {

  private String date;

  /**
   * Instantiates a new Date after predicate with a base date to check other dates with.
   *
   * @param date the date
   */
  public DateAfterPredicate(String date) {
    this.date = date;
  }

  /**
   * Test boolean class which returns true if given date is after(future) the base date.
   *
   * @param toCheck the to check
   * @return the boolean
   */
  public boolean test(String toCheck) {
    int check = this.date.compareTo(toCheck);
    return check <0;
  }
}
