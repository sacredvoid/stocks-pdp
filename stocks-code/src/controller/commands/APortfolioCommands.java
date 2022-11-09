package controller.commands;

import controller.IPortfolioCommands;

public abstract class APortfolioCommands implements IPortfolioCommands {
  protected String statusMessage;


  protected void setStatusMessage(String message) {
    this.statusMessage = message;
  }

  protected String getStatusMessage() {
    return this.statusMessage;
  }
}
