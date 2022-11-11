package model;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface ApiHandler {

  ApiHandler createURL();

  boolean works();

  void writeJson();

  String getStatus();
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
    } catch (NullPointerException e){
      return null;
    }

    return output;
  }
}
