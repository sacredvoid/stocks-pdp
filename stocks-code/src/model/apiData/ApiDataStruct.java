package model.apiData;

import java.util.HashMap;
import java.util.Map;

/**
 * ApiDataStruct class defines the structure of the API Stock Data.
 */
public class ApiDataStruct {

  private String open;
  private String high;
  private String low;
  private String close;
  private String volume;

  /**
   * get the Open value of the stock.
   *
   * @return open value as string
   */
  public String getOpen() {
    return open;
  }

  /**
   * get the High value of the stock.
   *
   * @return high value as string
   */
  public String getHigh() {
    return high;
  }

  /**
   * get the Low value of the stock.
   *
   * @return low value as string
   */
  public String getLow() {
    return low;
  }

  /**
   * get the Close value of the stock.
   *
   * @return close value as string
   */
  public String getClose() {
    return close;
  }

  /**
   * get the Volume value of the stock.
   *
   * @return
   */
  public String getVolume() {
    return volume;
  }
}
