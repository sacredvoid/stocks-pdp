package controller;

import model.ModelOrchestrator;
import model.Orchestrator;

public interface IPortfolioCommands {

  String getStatusMessage();

  void setStatusMessage(String message);

  boolean getIsTabularDataBoolean();
  void setIsTabularDataBoolean(boolean bool);

  void go(Orchestrator m);
}
