package model.validation;

import java.text.ParseException;

/**
 * IDataValidator interface defines method signatures for checking and validating dates.
 */
public interface IDataValidator {

  boolean checkData(String data) throws ParseException;
}
