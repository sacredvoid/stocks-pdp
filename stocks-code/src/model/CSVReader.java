package model;

import java.io.FileNotFoundException;

public interface CSVReader {
  String readFile(String path, String dir) throws FileNotFoundException;
}
