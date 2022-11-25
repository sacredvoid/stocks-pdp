package controller;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import model.AOrchestrator;
import model.Orchestrator;
import org.junit.Test;
import view.UserInteraction;

/**
 * The type Interaction handler mock test.
 */
public class InteractionHandlerMockTest {

  /**
   * The Ih.
   */
  InteractionHandlerV2 ih;
  /**
   * The Morch.
   */
  Orchestrator morch;
  private final String yellow = "\u001B[33m";
  private final String red = "\u001B[31m";
  private final String green = "\u001B[32m";
  private final String resetColor = "\u001B[0m";
  private final String eol = "\r\n";


  /**
   * The type Model orchestrator mock.
   */
  class ModelOrchestratorMock extends AOrchestrator {

    /**
     * The Commands entered.
     */
    StringBuilder commandsEntered = new StringBuilder();

    /**
     * Command log string.
     *
     * @return the string
     */
    public String commandLog() {
      return commandsEntered.toString();
    }

    @Override
    public String getLatestPortfolioComposition(String portfolioID) throws FileNotFoundException {
      commandsEntered.append("Command Success\n");
      return "Control came to getLatestPortfolioComposition()";
    }

    @Override
    public String createPortfolio(String portfolioData) {
      commandsEntered.append("Command Success\n");
      return "Control came to createPortfolio()";
    }

    @Override
    public String getPortfolioValue(String pfId, String date) throws ParseException {
      commandsEntered.append("Command Success\n");
      return "Control came to getPortfolioValue()";
    }

    @Override
    public String getPortfolioCompositionByDate(String date, String pfID)
        throws FileNotFoundException {
      commandsEntered.append("Command Success\n");
      return "Control came to getPortfolioCompositionByDate()";
    }


    @Override
    public String loadExternalPortfolio(String path) throws FileNotFoundException {
      commandsEntered.append("Command Success\n");
      return "Control came to loadExternamlPortfolio()";
    }

    @Override
    public String editExistingPortfolio(String pfID, String call) {
      commandsEntered.append("Command Success\n");
      return "Control came to loadExternalPortfolio()";
    }

    @Override
    public String showPerformance(String pfId, String startDate, String endDate)
        throws FileNotFoundException {
      commandsEntered.append("Command Success\n");
      return "Control came to showPerformance()";
    }

    @Override
    public String[] getCostBasis(String pfID, String date) {
      commandsEntered.append("Command Success\n");
      String[] result = {"fetched investment", "fetched comission", "fetched costbasis",
          "fetched wealth gain"};
      return result;
    }

    @Override
    public void setCommissionFees(String commissionFees) {
      commandsEntered.append("Command Success\n");
    }
  }

  /**
   * Gets portfolio test.
   */
  @Test
  public void getPortfolioTest() {
    Orchestrator morch = new ModelOrchestratorMock();
    Reader in = new StringReader(" 0 q q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    UserInteraction ui = new UserInteraction(out, morch);
    ih = new InteractionHandlerV2(in, morch, ui);

    ih.run();

    assertEquals("Control came to getPortfolioValue()", bytes.toString());
  }
}
