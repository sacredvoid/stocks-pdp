package controller.commands;

/**
 * The Abstract class for portfolio commands interface. It implements some of the common methods
 * that are used to get and set status/booleans.
 */
public abstract class APortfolioCommands implements IPortfolioCommands {

  /**
   * The Status message.
   */
  protected String statusMessage = "";
  /**
   * The determines if status message is tabular data.
   */
  protected boolean isTabularData = false;


  public void setStatusMessage(String message) {
    this.statusMessage = message;
  }

  public String getStatusMessage() {
    return this.statusMessage;
  }

  public void setIsTabularDataBoolean(boolean bool) {
    this.isTabularData = bool;
  }

  public boolean getIsTabularDataBoolean() {
    return this.isTabularData;
  }
}
