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

  public String readFile(String filename, String dir) throws FileNotFoundException {
    csvData = new ArrayList<>();
    csvStringData = new StringBuilder();
    String path = pathResolver(filename,dir);
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

  public void writeToFile(String filename, String dir, String data) throws IOException {

    try {
      String newPath = pathResolver(filename, dir);
      FileWriter myWriter = new FileWriter(newPath);
      myWriter.write(data);
      myWriter.close();
    } catch (IOException e) {
      throw new IOException(e.getMessage());
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
      int count =0;

      if(!isStockScrip(data[0]) && isNumeric(data[1])) {
        data[1] = String.valueOf(Math.round(Float.parseFloat(data[1])));
        csvData.add(List.of(data));
        for (String datapoint: data
        ) {
          csvStringData.append(datapoint.strip());
          if(!(count == data.length-1)) {
            csvStringData.append(",");
          }
          count+=1;
        }
        csvStringData.append("\n");
      }
      else {
        this.readStatus+=String.format("Invalid Data Group: %s,%s\n",data[0],data[1]);
      }
    }
  }

  private boolean isNumeric(String str) {
    try {
      Double.parseDouble(str);
      return true;
    } catch(NumberFormatException e){
      return false;
    }
  }

  private boolean isStockScrip(String str){
    String SCRIP_REGEX = "([A-za-z0-9])+([.]([A-za-z])+)?";
    return !str.matches(SCRIP_REGEX);
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

  public static void main(String[] args) throws IOException {
    CSVFileOps f = new CSVFileOps();
    f.writeToFile("test.csv",".\\PortfolioData","AAPL,10,adwd\nTSLA,20,d29u");
    System.out.printf(f.readFile("test.csv","PortfolioData"));
  }

}
