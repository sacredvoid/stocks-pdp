package model.fileops;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Define things like parse file(general for all kinds of file writes)
 */
public interface FileOps {

  String readFile(String filename, String dir) throws FileNotFoundException;

  void writeToFile(String filename, String dir, String data) throws IOException;

}
