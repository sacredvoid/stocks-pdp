package controller.commands;

import controller.IPortfolioCommands;

public abstract class APortfolioCommands implements IPortfolioCommands {

  protected String statusMessage = "";
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
