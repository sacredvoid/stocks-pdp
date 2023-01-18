package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import model.fileops.JSONFileOps;
import model.portfolio.StockData;
import org.junit.Before;
import org.junit.Test;

/**
 * The type Model orchestrator v 2 test.
 */
public class ModelOrchestratorV2Test {

  private Orchestrator model = new ModelOrchestratorV2();
  private JSONFileOps jsonFileOps = new JSONFileOps();

  /**
   * Sets .
   *
   * @throws IOException the io exception
   */
  @Before
  public void setup() throws IOException {

    StockData s1 = new StockData("AAPL", 10);

  }

  /**
   * Test cost basis.
   */
  @Test
  public void testCostBasis() {
    String[] costBasis = this.model.getCostBasis("test", "2022-11-16");
    // Test contains two transactions (APPL and TSLA)
    assertEquals("3663.8", costBasis[0]);
    assertEquals("2.0", costBasis[1]);
    assertEquals("0.0", costBasis[2]);
    assertEquals("3635.8", costBasis[3]);
    System.out.println(Arrays.toString(costBasis));
    //    assertEquals()
  }

  /**
   * Test invalid date transaction buy.
   *
   * @throws FileNotFoundException the file not found exception
   */
  @Test
  public void testInvalidDateTransactionBuy() throws FileNotFoundException {
    // test2 contains two transactions (AAPL,TSLA on 2022-11-16) and one GOOG transaction on
    // 2022-11-13 which is a weekend. This buy transaction will not reflect in the portfolio
    // (invalid dates are skipped)
    String loadedPortfolio = this.model.getPortfolioCompositionByDate("2022-11-16",
        "test2");
    assertEquals("AAPL,10.0\n"
        + "TSLA,10.0", loadedPortfolio);
    //    assertEquals()
  }

  /**
   * Test invalid date transaction sell.
   *
   * @throws FileNotFoundException the file not found exception
   */
  @Test
  public void testInvalidDateTransactionSell() throws FileNotFoundException {
    // test2 contains two transactions (AAPL,TSLA,GOOG on 2022-11-16) and one GOOG sell transaction
    // on 2022-11-13 which is a weekend. This GOOG sell transaction will not reflect in the
    // portfolio (invalid dates are skipped)
    String loadedPortfolio = this.model.getPortfolioCompositionByDate("2022-11-16",
        "test3");
    assertEquals("AAPL,10.0\n"
        + "TSLA,10.0\n"
        + "GOOG,10.0", loadedPortfolio);
    //    assertEquals()
  }

  /**
   * Create dca portfolio test.
   */
  @Test
  public void createDCAPortfolioTest(){
    String dcaInput = "strat1\n30\n1000\n2021-11-10\n2022-05-10\nAAPL,10\nMSFT,20";
    String status= this.model.createDCAPortfolio(dcaInput);
    System.out.println(status);
    if(!status.contains("Created")){
      fail();
    }
  }

  /**
   * Convert flexible to dca.
   */
  @Test
  public void convertFlexibleToDCA(){
    String dcaInput = "strat1\n30\n1000\n2021-11-10\n2022-11-10\nAAPL,10\nMSFT,20";
    String status = this.model.existingPortfolioToDCAPortfolio("190076",dcaInput);
    System.out.println(status);
    if(!status.contains("Modified")){
      fail();
    }
  }

  /**
   * Get costbasis test.
   */
  @Test
  public void getCostbasisTest(){
    String dcaInput = "strat1\n30\n1000\n2021-11-10\n2022-11-10\nAAPL,10\nMSFT,20";
    String createStatus = this.model.createDCAPortfolio(dcaInput);
    String[] statusSplit = createStatus.split(" ");

    String[] costBasis =
        this.model.getCostBasis(statusSplit[statusSplit.length-1],"2022-02-08");

    assertEquals("1200.0",costBasis[0]);
    assertEquals("8.0",costBasis[1]);
    assertEquals("0.0",costBasis[2]);
    assertEquals("1208.0",costBasis[3]);
  }

  /**
   * Multiple date cost basis test.
   */
  @Test
  public void multipleDateCostBasisTest(){
    String dcaInput = "strat1\n30\n1000\n2021-11-10\n2022-05-10\nAAPL,10\nMSFT,20";
    String createStatus = this.model.createDCAPortfolio(dcaInput);
    String[] statusSplit = createStatus.split(" ");

    String date1 = "2022-02-08";
    String date2 = "2022-12-01";
    String date3 = "2021-11-09";

    String[] costBasis =
        this.model.getCostBasis(statusSplit[statusSplit.length-1],date1);

    assertEquals("1200.0",costBasis[0]);
    assertEquals("8.0",costBasis[1]);
    assertEquals("0.0",costBasis[2]);
    assertEquals("1208.0",costBasis[3]);

    String [] costBasisForDateAfterEndRange = this.model.getCostBasis(statusSplit[statusSplit.length-1],date2);

    for (String s: costBasisForDateAfterEndRange
    ) {
      System.out.println(s);;
    };

    assertEquals("2100.0",costBasisForDateAfterEndRange[0]);
    assertEquals("14.0",costBasisForDateAfterEndRange[1]);
    assertEquals("0.0",costBasisForDateAfterEndRange[2]);
    assertEquals("2114.0",costBasisForDateAfterEndRange[3]);


    String [] costBasisForDateBeforeStartRange = this.model.getCostBasis(statusSplit[statusSplit.length-1],date3);

    assertEquals("No data before given date",costBasisForDateBeforeStartRange[0]);

  }

  /**
   * Dca portfolio value test.
   */
  @Test
  public void dcaPortfolioValueTest() {
    String dcaInput = "strat1\n30\n1000\n2021-11-10\n2022-05-10\nAAPL,10\nMSFT,20";
    String createStatus = this.model.createDCAPortfolio(dcaInput);
    String[] statusSplit = createStatus.split(" ");

    String date1 = "2022-02-08";
    String date2 = "2022-12-01";
    String date3 = "2021-11-09";

    try {
      String value1 = this.model.getPortfolioValue(date1, statusSplit[statusSplit.length - 1]);
      String value2 = this.model.getPortfolioValue(date2, statusSplit[statusSplit.length - 1]);
      String value3 = this.model.getPortfolioValue(date3, statusSplit[statusSplit.length - 1]);
      assertEquals("MSFT,2.4815488,755.78\n"
          + "AAPL,2.3860376,417.15\n"
          + "Total,-,1172.93",value1);
      assertEquals("MSFT,4.638883,1181.48\n"
          + "AAPL,4.2778263,634.44\n"
          + "Total,-,1815.92",value2);
      assertEquals("0. No Portfolio Data before given date. Sorry! enter date equal to or after 2021-11-10",value3);

    } catch (ParseException e) {
      fail();
    }
  }
}
