package model;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the model.
 */
public class ModelOrchestratorTest {

  ModelOrchestrator morch;

  @Before
  public void setup() {
    morch = new ModelOrchestrator();
  }

  private boolean deleteFile(String portID) {
    String osSep = OSValidator.getOSSeparator();
    String basePath = "." + osSep + "app_data" + osSep + "PortfolioData" + osSep + portID + ".csv";
    File f = new File(basePath);
    return f.delete();
  }

  @Test
  public void testGetPortfolio() {
    String out = "";
    try {
      out = morch.getPortfolio("321123");
    } catch (FileNotFoundException ignored) {
    }
    assertEquals(
        "AAPL,20\n" +
            "MSFT,300000\n" +
            "GOOG,20\n" +
            "TSLA,0\n" +
            "MSFT,20\n" +
            "GPV.TRV,31\n" +
            "CNA.LON,2001", out.strip());
    try {
      out = morch.getPortfolio("777777");
    } catch (FileNotFoundException f) {
      System.out.println("File not found as expected");
    }
  }

  @Test
  public void testCreatePortfolio() {
    String data = "AAPL,20\n" +
        "MSFT,30";
    String message = morch.createPortfolio(data);
    String savedData = "";
    String portID = message.split(":")[1].split(" ")[1];
    try {
      savedData = morch.getPortfolio(portID);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
    assertEquals(data.strip(), savedData.strip());
    if (deleteFile(portID)) {
      System.out.println("Deleted test file");
    } else {
      System.out.println("Could not delete test file");
    }
  }

  @Test
  public void testGeneratePortfolioID() {
    String generatedID = morch.generatePortfolioID();
    String digitRegex = "[0-9]{6}";
    assertEquals(6, generatedID.length());
    assertTrue(generatedID.matches(digitRegex));
  }

  @Test
  public void testGetPortfolioValue() {
    // Weekend test
    String portfolioValue = "";
    try {
      portfolioValue = morch.getPortfolioValue("2022-10-15", morch.getPortfolio("321123"));
    } catch (FileNotFoundException | ParseException ignored) {
    }
    assertNull(portfolioValue);
    try {
      portfolioValue = morch.getPortfolioValue("2020-10-05", morch.getPortfolio("321123"));
    } catch (ParseException | FileNotFoundException ignored) {
    }
    assertEquals("AAPL,20,2330.00\n" +
        "MSFT,300000,63114000.00\n" +
        "GOOG,20,29720.40\n" +
        "TSLA,0,NA\n" +
        "MSFT,20,4207.60\n" +
        "GPV.TRV,31,532.27\n" +
        "CNA.LON,2001,82481.22\n" +
        "Total,-,63233268.00", portfolioValue.strip());
  }

  @Test
  public void testShowExistingPortfolios() {
    String[] message = morch.showExistingPortfolios();
    File f = new File(".\\app_data\\PortfolioData");
    String[] expected = f.list();
    assert expected != null;
    assertEquals(expected[0], message[0]);
  }

  @Test
  public void testLoadExternalCSV() {
    String path = "D:\\PDP_CS5010\\stocks-pdp\\stocks-code\\app_data\\testFiles\\794762.csv";
    String message = "";
    try {
      message = morch.loadExternalCSV(path);
    } catch (FileNotFoundException e) {
      System.out.println("File not found!");
      fail();
    }
    assertNotEquals(message, "Failed to load");
    deleteFile(message);
  }

}
