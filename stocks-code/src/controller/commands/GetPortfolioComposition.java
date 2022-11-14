package controller.commands;

import java.io.FileNotFoundException;
import model.Orchestrator;

public class GetPortfolioComposition extends APortfolioCommands{

  private String pfID;

  public GetPortfolioComposition(String pfID) {
    this.pfID = pfID;
  }

  @Override
  public void go(Orchestrator m) {
    try {
      String pfData = m.getLatestPortfolioComposition(this.pfID);
      setStatusMessage(pfData);
    }
    catch (FileNotFoundException f) {
      setStatusMessage("File not found!");
    }
  }
}
