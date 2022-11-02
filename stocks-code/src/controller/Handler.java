package controller;

/**
 * The handler interface provides the required methods to write your own controller that
 * communicates between the model and view classes of your project.
 */
public interface Handler {

  /**
   * The run function takes control of flow and this is where you should define your applications
   * workflow: passing input from user to your model/view.
   */
  void run();
}
