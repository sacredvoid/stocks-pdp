package model.fileops;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import model.portfolio.PortfolioDateMapper;

public class JSONFileOps extends AFileOps {

  private Gson customJSONReader;
  private PortfolioDateMapper pfData;

  public JSONFileOps(PortfolioDateMapper pfData) {
    customJSONReader = new GsonBuilder().setPrettyPrinting().create();
    this.pfData = pfData;
  }

  @Override
  public String readFile(String filename, String dir) throws FileNotFoundException {
    String path = pathResolver(filename, dir);
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      pfData = customJSONReader.fromJson(br,PortfolioDateMapper.class);
      return pfData.toString();
    }
    catch (Exception e) {
      throw new FileNotFoundException("Portfolio File not found!");
    }
  }

  @Override
  public void writeToFile(String filename, String dir, String data) throws IOException {
    String path = pathResolver(filename,dir);
    try (Writer writer = Files.newBufferedWriter(Path.of(path), StandardCharsets.UTF_8)) {
      customJSONReader.toJson(data,writer);
    }
    catch (Exception e) {
      throw new IOException(e.getMessage());
    }
  }
}
