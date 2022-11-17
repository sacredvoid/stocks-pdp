package controller.commands;

import java.text.ParseException;
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
      setStatusMessage(m.getPortfolioValue(date, pfID));
      setIsTabularDataBoolean(true);
    } catch (ParseException f) {
      setStatusMessage("Unable to read file");
    }

  }
}
