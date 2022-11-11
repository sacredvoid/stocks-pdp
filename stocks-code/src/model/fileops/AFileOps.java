package model.fileops;

import java.io.File;
import model.OSValidator;

public abstract class AFileOps implements FileOps {

  private String osPathSeparator = OSValidator.getOSSeparator();


  /**
   * Internal method to construct a complete path to file, given a path and directory (sitting
   * inside './app_data').
   *
   * @param inputFilename name of the file in string
   * @param dir           name of the dir in string (sitting inside './app_data')
   * @return the relative path to the file
   */
  protected String pathResolver(String inputFilename, String dir) {
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
  protected void makeDirs(String path) {
    new File(path).getParentFile().mkdirs();
  }
}
