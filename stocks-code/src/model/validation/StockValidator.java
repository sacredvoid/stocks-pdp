package model.validation;

/**
 * StockValidator class defines the methods to validate stock names.
 */
public class StockValidator implements IDataValidator {

  /**
   * Our internal method to check if the given Stock Ticker is a valid ticker or not. It follows the
   * rule: Atleast 1 alphabet/number followed by an optional dot followed by atleast one alphabet.
   * We used this to include stocks from other stock exchanges.
   *
   * @param str the stock ticker symbol
   * @return true/false if the ticker symbol matches the regex
   */
  public boolean checkData(String str) {
    String scripRegex = "([A-Z])+([.]([A-Z])+)?";
    return !str.matches(scripRegex);
  }
}
