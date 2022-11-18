package model.fileops;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The Json file ops class that extends Abstract File Ops and reads/writes a given JSON
 * file/Object.
 */
public class JSONFileOps extends AFileOps {

  private Gson customJSONReader;

  /**
   * Instantiates a new Json file ops.
   */
  public JSONFileOps() {
    customJSONReader = new GsonBuilder().setPrettyPrinting().create();
  }

  @Override
  public String readFile(String filename, String dir) throws FileNotFoundException {
    String path = pathResolver(filename, dir);
    try (Reader reader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
      JsonObject j = customJSONReader.fromJson(reader, JsonObject.class);
      return j.toString();
    } catch (Exception e) {
      throw new FileNotFoundException("Portfolio File not found!");
    }
  }

  @Override
  public void writeToFile(String filename, String dir, String data) throws IOException {
    String staticDataPath = pathResolver("", dir);

    Path staticPath = Path.of(staticDataPath);
    if (!Files.isDirectory(staticPath)) {
      Files.createDirectory(staticPath);
    }

    String dataWritePath = pathResolver(filename, dir);
    try (Writer writer = Files.newBufferedWriter(Path.of(dataWritePath), StandardCharsets.UTF_8)) {
      JsonElement json = JsonParser.parseString(data);
      customJSONReader.toJson(json, writer);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
  }

}
