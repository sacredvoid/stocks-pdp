//package controller;
//
//import java.io.FileNotFoundException;
//import java.text.ParseException;
//import model.AOrchestrator;
//import model.Orchestrator;
//import org.junit.Before;
//import org.junit.Test;
//
//public class InteractionHandlerMockTest {
//
//  @Before
//  public void setup(){
//
//    class modelOrchestratorMock extends AOrchestrator {
//
//      @Override
//      public String getPortfolio(String portfolioID) throws FileNotFoundException {
//        return null;
//      }
//
//      @Override
//      public String createPortfolio(String portfolioData) {
//        return null;
//      }
//
//      @Override
//      public String getPortfolioValue(String pfId, String date) throws ParseException {
//        return null;
//      }
//
//      @Override
//      public String getPortfolioValueByID(String date, String pfID) throws FileNotFoundException {
//        return null;
//      }
//
//      @Override
//      public String loadExternalPortfolio(String path) throws FileNotFoundException {
//        return null;
//      }
//    }
//  }
//  @Test
//  public void getPortfolioTest(){
//
//  }
//}
