package model;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
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

}
