package controller.commands;

import java.io.FileNotFoundException;
import model.Orchestrator;

/**
 * The command to Get portfolio composition for a given Portfolio ID.
 */
public class GetPortfolioComposition extends APortfolioCommands {

  private String pfID;

  /**
   * Instantiates a new Get portfolio composition and sets it's PFid.
   *
   * @param pfID the pf id
   */
  public GetPortfolioComposition(String pfID) {
    this.pfID = pfID;
  }

  @Override
  public void runCommand(Orchestrator m) {
    try {
      String pfData = m.getLatestPortfolioComposition(this.pfID);
      setStatusMessage(pfData);
      setIsTabularDataBoolean(true);
    } catch (FileNotFoundException f) {
      setStatusMessage("File not found!");
    }
  }
}
