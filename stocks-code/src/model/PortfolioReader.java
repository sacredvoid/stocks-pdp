package model;

// User, Sam
// PortfolioID, ____
// Stock, Quantity
// AAPL, 10
// TSLA, 20

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the user data of the portfolio
 */
public class PortfolioReader implements CSVReader {

  private String readStatus;

  final String splitBy = ",";
  private List<List<String>> csvData = new ArrayList<>();

  public void readFile(String path) {

    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      String currentLine = "";
      while ((currentLine = br.readLine()) != null)   //returns a Boolean value
      {
        String[] data = currentLine.split(splitBy);    // use comma as separator
        // Check data
        dataCheck(data);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void dataCheck(String[] data) {
    if (data.length == 1) {
      if (!data[0].isEmpty()) {
        this.readStatus = "Missing Data: "+data[0];
        // Ignoring this stock?
      }
    }
    if(data.length > 1) {
      csvData.add(List.of(data));
    }
  }

  public static void main(String[] args) {
    PortfolioReader pr = new PortfolioReader();
    pr.readFile(".\\PortfolioData\\myPort.csv");
    System.out.println(pr.csvData.toString());
  }

}