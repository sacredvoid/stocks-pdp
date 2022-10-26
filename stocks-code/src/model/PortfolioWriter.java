package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes data of our user to the portfolios
 */
public class PortfolioWriter {

  private final String osPathSeparator;

  public PortfolioWriter() {
    this.osPathSeparator = OSValidator.getOSSeparator();
  }

  /**
   * This function expects a Path to File and String data (generally a JSONObject.toString())
   * @param path string that contains filename
   * @param data string that contains json data
   */
  public void writeToFile(String path, String data) {

    try {
      String newPath = pathResolver(path);
      FileWriter myWriter = new FileWriter(newPath);
      myWriter.write(data);
      myWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  private String pathResolver(String inputFilename) {
    // Check if inputFilename exists
    StringBuilder path = new StringBuilder("." + this.osPathSeparator + "PortfolioData");
    path.append(osPathSeparator).append(inputFilename);
    makeDirs(path.toString());
    return path.toString();
  }

  private void makeDirs(String path) {
    new File(path).getParentFile().mkdirs();
  }

  public static void main(String[] args) {
    PortfolioWriter f = new PortfolioWriter();
    f.writeToFile("myPort.csv","AAPL,10\nTSLA,20");
  }

}