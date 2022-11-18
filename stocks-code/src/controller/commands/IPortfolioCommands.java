package controller.commands;

import model.Orchestrator;

/**
 * The Portfolio Commands interface that defines the basic attributes and methods that every command
 * class should implement. These commands are inspired from the Command Pattern. Every command has a
 * status and flag that helps the controller determine if it was successful or not.
 */
public interface IPortfolioCommands {

  /**
   * This gets the status message for the particular command class.
   *
   * @return String of status message
   */
  String getStatusMessage();

  /**
   * This sets the status message for the particular command class.
   *
   * @param message takes an input message that you want to set
   */
  void setStatusMessage(String message);

  /**
   * This is to set the tabular data flag which is used by the controller to determine the type of
   * data the view should print.
   *
   * @return returns a boolean telling if the data returned from command is tabular or not
   */
  boolean getIsTabularDataBoolean();

  /**
   * This sets the tabular data flag which is used by the controller to determine the type of data
   * the view should print.
   *
   * @param bool the boolean indicating the return data type of command's resulting string
   */
  void setIsTabularDataBoolean(boolean bool);

  /**
   * General method to start the command and call the respective model object from the command.
   *
   * @param m Orchestrator (model) object
   */
  void runCommand(Orchestrator m);
}
