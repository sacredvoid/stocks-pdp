package controller;

import model.ModelOrchestrator;
import model.Orchestrator;

public interface IPortfolioCommands {

  String getStatusMessage();
  void setStatusMessage(String message);
  void go(Orchestrator m);
}
