package controller.commands;

import controller.IPortfolioCommands;
import java.io.FileNotFoundException;
import java.text.ParseException;
import model.ModelOrchestrator;
import model.Orchestrator;

public class GetPortfolioValue extends APortfolioCommands {

  private String pfID;
  private String date;

  public GetPortfolioValue(String pfID, String date) {
    this.pfID = pfID;
    this.date = date;
  }
  @Override
  public void go(Orchestrator m) {
    try {
      setStatusMessage(m.getPortfolioValue(date,pfID));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

  }
}
