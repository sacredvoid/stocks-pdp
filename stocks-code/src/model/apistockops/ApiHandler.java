package model.apistockops;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * ApiHandler interface defines the method signatures required for integrating and <p></p> multiple
 * API sources.
 */
public interface ApiHandler {

  /**
   * createURL() method is responsible for building the URL for the given stock ticker.
   *
   * @return the same ApiHandler object with URL created.
   */
  ApiHandler createURL();

  /**
   * works() method is responsible for checking whether the URL fetches data successfully or<p></p>
   * runs into errors.
   *
   * @return true if fetching data from the URL was successful else false
   */
  boolean works();

  /**
   * writeJson() method is responsible for writing the fetched Json data to a file in<p></p> in the
   * local machine.
   */
  void writeJson();

  /**
   * getStatus() returns the status of the Api data fetching process.
   *
   * @return
   */
  String getStatus();

  /**
   * fetch() method is responsible for fetching the data from the API using the URL created<p></p>
   * by the createURL() method.
   *
   * @param url API call URL for the respective stock ticker
   * @return the complete data fetched from the API
   */
  default StringBuilder fetch(URL url) {
    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {

      /*
      Execute this query. This returns an InputStream object.
      In the csv format, it returns several lines, each line being separated
      by commas. Each line contains the date, price at opening time, highest
      price for that date, lowest price for that date, price at closing time
      and the volume of trade (no. of shares bought/sold) on that date.

      This is printed below.
       */
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
//
      return null;
    } catch (NullPointerException e) {
      return null;
    }

    return output;
  }
}
