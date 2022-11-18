package controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import model.AOrchestrator;
import model.ModelOrchestratorV2;
import model.Orchestrator;
import org.junit.Before;
import org.junit.Test;
import view.UserInteraction;

public class InteractionHandlerMockTest {

  InteractionHandlerV2 ih;
  Orchestrator morch;
  private final String yellow = "\u001B[33m";
  private final String red = "\u001B[31m";
  private final String green = "\u001B[32m";
  private final String resetColor = "\u001B[0m";
  private final String eol = "\r\n";


    class modelOrchestratorMock extends AOrchestrator {

      StringBuilder commandsEntered = new StringBuilder();
      public String commandLog(){
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
        String [] result = {"fetched investment","fetched comission","fetched costbasis","fetched wealth gain"};
        return result;
      }

      @Override
      public void setCommissionFees(String commissionFees) {
        commandsEntered.append("Command Success\n");
      }
//
//    morch = new modelOrchestratorMock();
  }
  @Test
  public void getPortfolioTest(){
    Orchestrator morch = new modelOrchestratorMock();
    Reader in = new StringReader(" 0 q q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    UserInteraction ui = new UserInteraction(out,morch);
    ih = new InteractionHandlerV2(in,morch,ui);

    ih.run();

    System.out.println(new String(bytes.toByteArray()).strip());

  }
}
