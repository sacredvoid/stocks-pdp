package controller;

import java.util.Scanner;
import model.Orchestrator;
import view.UserInteraction;

/**
 * Our main controller which lets user decide between text based and GUI controller.
 */
public class MainController extends AbstractHandler {

  private Orchestrator model;

  /**
   * Instantiates a new Main controller.
   *
   * @param input the input Scanner
   * @param model the model object
   * @param ui    the ui object
   */
  public MainController(Readable input, Orchestrator model, UserInteraction ui) {
    super.ui = ui;
    this.model = model;
    this.scan = new Scanner(input);
  }

  @Override
  public void run() {
    this.ui.printText("0 - Text Based Interface", "G");
    this.ui.printText("1 - GUI Based Interface", "G");
    String input = scan.next();
    if (input.equalsIgnoreCase("q")) {
      return;
    }

    if (input.equals("0")) {
      TextUIHandler textUI = new TextUIHandler(this.scan, this.model, this.ui);
      textUI.run();
    } else {
      new GraphicalUIHandler(model).run();
    }

  }
}
