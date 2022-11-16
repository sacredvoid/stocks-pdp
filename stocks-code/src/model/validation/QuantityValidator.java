package model.validation;

/**
 * QuantitiyValidator class defines the methods to validate quantity parameter.
 */
public class QuantityValidator implements IDataValidator {

  /**
   * Our internal method to check if the given stock quantity is numeric or not.
   *
   * @param data stock quantity data as string
   * @return true/false depending on if string passed to it is numeric.
   */
  @Override
  public boolean checkData(String data) {
    try {
      Double.parseDouble(data);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
