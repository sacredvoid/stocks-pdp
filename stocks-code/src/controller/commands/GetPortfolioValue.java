package controller.commands;

import java.text.ParseException;
import model.Orchestrator;

/**
 * The command to Get portfolio value of the user.
 */
public class GetPortfolioValue extends APortfolioCommands {

  private String pfID;
  private String date;

  /**
   * Instantiates a new Get portfolio value with user input PFid and date.
   *
   * @param pfID the pf id
   * @param date the date
   */
  public GetPortfolioValue(String pfID, String date) {
    this.pfID = pfID;
    this.date = date;
  }

  @Override
  public void runCommand(Orchestrator m) {
    try {
      setStatusMessage(m.getPortfolioValue(date, pfID));
      setIsTabularDataBoolean(true);
    } catch (ParseException f) {
      setStatusMessage("Unable to read file");
    }

  }
}
