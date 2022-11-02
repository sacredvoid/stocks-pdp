package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The CSVFileOps class which is used to Read/Write CSV files and perform data check operations
 * (like validating the Stock,Quantity which our application ingests.
 */
public class CSVFileOps {

  private String readStatus;
  final String splitBy = ",";
  private final String osPathSeparator;
  private List<List<String>> csvData;
  private StringBuilder csvStringData;


  /**
   * The default constructor for our class which just initializes the current OS's path separator
   * (\\ for windows and / for linux).
   */
  public CSVFileOps() {
    this.osPathSeparator = OSValidator.getOSSeparator();
  }

  /**
   * Reads a CSV file given it's filename and dir. By default, it's only designed to look inside
   * ./app_data/ as a security feature. The 'dir' is the name of the folder sitting inside
   * './app_data'.
   *
   * @param filename the name of csv file in string
   * @param dir      the dir of the csv file in string
   * @return the contents of the CSV file as String
   * @throws FileNotFoundException throws when file not found in the given directory
   */
  public String readFile(String filename, String dir) throws FileNotFoundException {
    csvData = new ArrayList<>();
    csvStringData = new StringBuilder();
    String path = pathResolver(filename, dir);
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      String currentLine = "";
      while ((currentLine = br.readLine()) != null)   //returns a Boolean value
      {
        String[] data = currentLine.split(splitBy);    // use comma as separator
        // Check data
        if (data.length <= 2) {
          dataCheck(data);
        } else {
          csvStringData.append(currentLine).append("\n");
        }
      }
      this.readStatus = "Success";
    } catch (IOException e) {
      throw new FileNotFoundException("Portfolio File not found!");
    }
    return csvStringData.toString();
  }

  /**
   * Writes given CSV data (as String) into a file with given 'filename' and 'dir' parameters. By
   * default the 'dir' provided will be created inside "./app_data/", there's no option given to
   * change this for security measures.
   *
   * @param filename name of the file you want to save as
   * @param dir      name of the dir you want to save the file to (sitting in ./app_data)
   * @param data     CSV data in a String datatype you want to write
   * @throws IOException throws when it's unable to save the CSV file
   */
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

  /**
   * Getter method to return a nested list of CSV Data that has been read by readFile method.
   *
   * @return a list of list of string of CSV Data
   */
  public List<List<String>> getCsvData() {
    return this.csvData;
  }

  /**
   * Our internal data validator method that checks for CSV data that our application can ingest
   * (like only integer stock quantities, String scrip names that match a regex to name a few). It
   * discards missing data rows from the CSV file.
   *
   * @param data a list of String of data containing comma separated column values (Stock,Quantity
   *             generally)
   */
  private void dataCheck(String[] data) {
    if (data.length == 1) {
      if (!data[0].isEmpty()) {
        this.readStatus = "Missing Data: " + data[0];
        // Ignoring this stock?
      }
    }
    if (data.length > 1) {
      int count = 0;

      if (!isStockScrip(data[0]) && isNumeric(data[1])) {
        data[1] = String.valueOf(Math.abs(Math.round(Float.parseFloat(data[1]))));
        csvData.add(List.of(data));
        for (String datapoint : data
        ) {
          csvStringData.append(datapoint.strip());
          if (!(count == data.length - 1)) {
            csvStringData.append(",");
          }
          count += 1;
        }
        csvStringData.append("\n");
      } else {
        this.readStatus += String.format("Invalid Data Group: %s,%s\n", data[0], data[1]);
      }
    }
  }

  /**
   * Our internal method to check if the given stock quantity is numeric or not.
   *
   * @param str stock quantity data as string
   * @return true/false depending on if string passed to it is numeric.
   */
  private boolean isNumeric(String str) {
    try {
      Double.parseDouble(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Our internal method to check if the given Stock Ticker is a valid ticker or not. It follows the
   * rule: Atleast 1 alphabet/number followed by an optional dot followed by atleast one alphabet.
   * We used this to include stocks from other stock exchanges.
   *
   * @param str the stock ticker symbol
   * @return true/false if the ticker symbol matches the regex
   */
  private boolean isStockScrip(String str) {
    String SCRIP_REGEX = "([A-Z])+([.]([A-Z])+)?";
    return !str.matches(SCRIP_REGEX);
  }

  /**
   * Internal method to construct a complete path to file, given a path and directory (sitting
   * inside './app_data').
   *
   * @param inputFilename name of the file in string
   * @param dir           name of the dir in string (sitting inside './app_data')
   * @return the relative path to the file
   */
  private String pathResolver(String inputFilename, String dir) {
    // Check if inputFilename exists
    StringBuilder path = new StringBuilder();

    if (!dir.isEmpty()) {
      path.append(".").append(this.osPathSeparator).append("app_data").append(this.osPathSeparator)
          .append(dir);
      path.append(osPathSeparator).append(inputFilename);
      makeDirs(path.toString());
    } else {
      path.append(inputFilename);
    }

    return path.toString();
  }

  /**
   * Our internal method to make sub-directories (for a file) if they don't already exist.
   *
   * @param path the path to file
   */
  private void makeDirs(String path) {
    new File(path).getParentFile().mkdirs();
  }
}
