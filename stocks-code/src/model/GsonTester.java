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
import java.util.HashMap;
import java.util.List;
import model.portfolio.PortfolioDateMapper;
import model.portfolio.PortfolioData;
import model.portfolio.StockData;

public class GsonTester {
  public static void main(String[] args) throws IOException {
    Gson g = new Gson();
    StockData s1 = new StockData("AAPL",20);
    StockData s2 = new StockData("TSLA",30);
    s1.setQuantity(36);
    s2.setQuantity(1);
    List<StockData> s = new ArrayList<>();
    s.add(s1);
    s.add(s2);
    PortfolioData p = new PortfolioData(s,123,234);
    System.out.printf("Portfolio stuff: %s",p.getQuantity("AAPL"));
    System.out.printf("Portfolio stuff: %s",p.getStockList());
    System.out.printf("%s",p.setQuantity("MSFT",30));
    p.addStock(new StockData("MSFT",32));
    System.out.printf("%s",p.getQuantity("LOVE"));
    System.out.printf("%s",p.setQuantity("MSFT",30));
    HashMap<String, PortfolioData> datePFmap = new HashMap<>();
    datePFmap.put("2020-10-11",p);
    datePFmap.put("2021-10-02",p);
    String fileName = "p1.json";
    Path path = Paths.get(fileName);

    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      Gson gson = new GsonBuilder()
          .setPrettyPrinting().create();
      gson.toJson(datePFmap, writer);
    }

    PortfolioDateMapper pf = new Gson().fromJson(new FileReader("D:\\PDP_CS5010\\stocks-pdp\\stocks-code\\p1.json"), PortfolioDateMapper.class);
  }
}
