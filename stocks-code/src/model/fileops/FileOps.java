package model.fileops;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Define things like parse file(general for all kinds of file writes).
 */
public interface FileOps {

  /**
   * Reads the given file and returns the data as a string.
   *
   * @param filename the filename
   * @param dir      the dir
   * @return the string
   * @throws FileNotFoundException the file not found exception
   */
  String readFile(String filename, String dir) throws FileNotFoundException;

  /**
   * Writes the given string data to file.
   *
   * @param filename the filename
   * @param dir      the dir
   * @param data     the data
   * @throws IOException the io exception
   */
  void writeToFile(String filename, String dir, String data) throws IOException;

}
