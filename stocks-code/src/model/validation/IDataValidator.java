package model.validation;

import java.text.ParseException;

public interface IDataValidator {

  boolean checkData(String data) throws ParseException;
}
