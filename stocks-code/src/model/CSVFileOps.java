package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVFileOps {

  private String readStatus;
  final String splitBy = ",";
  private final String osPathSeparator;
  private List<List<String>> csvData;
  private StringBuilder csvStringData;


  public CSVFileOps() {
    this.osPathSeparator = OSValidator.getOSSeparator();
  }

  public String readFile(String path) throws FileNotFoundException {
    csvData = new ArrayList<>();
    csvStringData = new StringBuilder();
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      String currentLine = "";
      while ((currentLine = br.readLine()) != null)   //returns a Boolean value
      {
        String[] data = currentLine.split(splitBy);    // use comma as separator
        // Check data
        dataCheck(data);
      }
      this.readStatus = "Success";
    }
    catch (IOException e)
    {
      throw new FileNotFoundException("Portfolio File not found!");
    }
    return csvStringData.toString();
  }

  public void writeToFile(String filename, String dir, String data) {

    try {
      String newPath = pathResolver(filename, dir);
      FileWriter myWriter = new FileWriter(newPath);
      myWriter.write(data);
      myWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public List<List<String>> getCsvData() {
    return this.csvData;
  }

  public String getReadStatus() {
    return readStatus;
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
      csvStringData.append(String.join(",",data[0],data[1])).append("\n");
    }
  }

  private String pathResolver(String inputFilename, String dir) {
    // Check if inputFilename exists
    StringBuilder path = new StringBuilder("."
        + this.osPathSeparator
        + "app_data"
        + this.osPathSeparator
        +dir);
    path.append(osPathSeparator).append(inputFilename);
    makeDirs(path.toString());
    return path.toString();
  }

  private void makeDirs(String path) {
    new File(path).getParentFile().mkdirs();
  }

  public static void main(String[] args) throws FileNotFoundException {
    CSVFileOps f = new CSVFileOps();
    f.writeToFile("test.csv",".\\PortfolioData","AAPL,10\nTSLA,20");
    System.out.printf(f.readFile(".\\app_data\\PortfolioData\\test.csv"));
  }

}
