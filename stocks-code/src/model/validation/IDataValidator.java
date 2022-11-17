package model.validation;

import java.text.ParseException;

/**
 * IDataValidator interface defines method signatures for checking and validating dates.
 */
public interface IDataValidator {

  /**
   * This is a general method that takes in the given data and returns true if it satisfies the
   * constraints, else false.
   *
   * @param data the data
   * @return boolean
   * @throws ParseException the parse exception
   */
  boolean checkData(String data) throws ParseException;
}
