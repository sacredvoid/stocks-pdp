package model.fileops;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import model.portfolio.PortfolioData;

public class JSONFileOps extends AFileOps {

  private Gson customJSONReader;

  public JSONFileOps() {
    customJSONReader = new GsonBuilder().setPrettyPrinting().create();
  }

  @Override
  public String readFile(String filename, String dir) throws FileNotFoundException {
    String path = pathResolver(filename, dir);
    try(Reader reader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
      JsonObject j = customJSONReader.fromJson(reader,JsonObject.class);
      return j.toString();
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

  public static void main(String[] args) throws IOException {
    JSONFileOps j = new JSONFileOps();
    String out = j.readFile("1234.json","PortfolioData");
    // Custom type Hashmap<String,PortfolioData>
//    Type
    Type typetoken = new TypeToken<HashMap<String,PortfolioData>>() {}.getType();
    Map<String,PortfolioData> pf = new Gson().fromJson(out,typetoken);
//    for(Entry<String, PortfolioData> entry:pf.entrySet()){
//      entry.getValue().ge;
//    }
    System.out.printf("****\n%s****\n",pf.get("2020-10-11").getStockList().get(0).getStockName());
    System.out.println(out);
    j.writeToFile("2345.json","PortfolioData",out);
  }

}
