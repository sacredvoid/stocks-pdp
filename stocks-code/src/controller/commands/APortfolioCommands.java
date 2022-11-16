package controller.commands;

import controller.IPortfolioCommands;

public abstract class APortfolioCommands implements IPortfolioCommands {

  protected String statusMessage = "";


  public void setStatusMessage(String message) {
    this.statusMessage = message;
  }

  public String getStatusMessage() {
    return this.statusMessage;
  }
}
