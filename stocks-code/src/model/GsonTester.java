package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.portfolio.PortfolioData;
import model.portfolio.StockData;

public class GsonTester {

  public static void main(String[] args) throws IOException {
    Gson g = new Gson();
    StockData s1 = new StockData("AAPL", 20);
    StockData s2 = new StockData("TSLA", 30);
    s1.setQuantity(36);
    s2.setQuantity(1);
    List<StockData> s = new ArrayList<>();
    s.add(s1);
    s.add(s2);
    PortfolioData p = new PortfolioData(s, 123, 234);
    System.out.printf("Portfolio stuff: %s", p.getQuantity("AAPL"));
    System.out.printf("Portfolio stuff: %s", p.getStockList());
    System.out.printf("%s", p.setQuantity("MSFT", 30));
    p.addStock(new StockData("MSFT", 32));
    System.out.printf("%s", p.getQuantity("LOVE"));
    System.out.printf("%s", p.setQuantity("MSFT", 30));
    Map<String, PortfolioData> datePFmap = new HashMap<>();
    datePFmap.put("2020-10-11", p);
    datePFmap.put("2020-11-10", p);
    datePFmap.put("1920-10-11", p);
    datePFmap.put("2020-01-01", p);
    datePFmap.put("2010-01-30", p);
    datePFmap.put("2021-10-02", p);
    String fileName = "p1.json";
    Path path = Paths.get(fileName);

    ArrayList<String> sortedDates = new ArrayList<>(datePFmap.keySet());
    sortedDates.sort(Collections.reverseOrder());
    System.out.println("**********************************");
    for (String x : sortedDates) {
      System.out.println("Key: = " + x);
    }
    System.out.println("**********************************");

    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      Gson gson = new GsonBuilder()
          .setPrettyPrinting().create();
      gson.toJson(datePFmap, writer);
    }

    HashMap<String, PortfolioData> pf = new Gson().fromJson(
        new FileReader("D:\\PDP_CS5010\\stocks-pdp\\stocks-code\\p1.json"), HashMap.class);
  }
}
