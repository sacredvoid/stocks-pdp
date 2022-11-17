package model.fileops;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.OSValidator;
import model.validation.DateValidator;
import model.validation.IDataValidator;
import model.validation.QuantityValidator;

/**
 * The CSVFileOps class which is used to Read/Write CSV files and perform data check operations
 * (like validating the Stock,Quantity which our application ingests.
 */
public class CSVFileOps extends AFileOps {

  private String readStatus;
  final String splitBy = ",";
  private final String osPathSeparator;
  private List<List<String>> csvData;
  private StringBuilder csvStringData;

  private IDataValidator dateValidator = new DateValidator();
  private QuantityValidator quantityValidator = new QuantityValidator();

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
      while ((currentLine = br.readLine()) != null) {
        String[] data = currentLine.split(splitBy);    // use comma as separator
        // Check data !!! Modification required
        if (data.length <= 2) {
          try {
            dataCheck(data);
          } catch (Exception p) {
            return "Some error! while reading";
          }
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
  List<List<String>> getCsvData() {
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

  // !!!! Needs modification
  private void dataCheck(String[] data) throws Exception {
    if (data.length == 1) {
      if (!data[0].isEmpty()) {
        this.readStatus = "Missing Data: " + data[0];
        // Ignoring this stock?
      }
    }
    if (data.length > 1) {
      int count = 0;

      if (!dateValidator.checkData(data[0]) && quantityValidator.checkData(data[1])) {
        data[1] = String.valueOf(Math.abs(Math.round(Float.parseFloat(data[1]))));
        csvData.add(List.of(data));
        for (String datapoint : data
        ) {
          csvStringData.append(datapoint.strip());
          if (count != data.length - 1) {
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
}
