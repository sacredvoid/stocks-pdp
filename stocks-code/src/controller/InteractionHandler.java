package controller;

import java.io.IOException;
import java.util.Scanner;
import view.UserInteraction;

/**
 * Controller Class which accepts input from the terminal and decides what to do accordingly.
 * This class talks to the Runner class which is the main program orchestrator.
 */
public class InteractionHandler {
  private final Readable in;
  private final Appendable out;

  //Place-holder for model
  private Object model;
  private UserInteraction ui;

  public InteractionHandler(Object model, UserInteraction ui, Readable input, Appendable output) {
    this.in = input;
    this.out = output;
    this.model = model;
    this.ui = ui;
  }

  public void getInput(String regex) throws IOException {
    Scanner scan = new Scanner(this.in);
    while (true) {
      validateInput(regex, scan);
      String currentInput = scan.next();
      if ("q".equals(currentInput)) {
        return;
      } else {
        this.out.append(currentInput);
        //
      }
    }
  }

  public String getOutput() {
    return this.out.toString();
  }

  // Grey area, unsure of how to proceed (or brain isn't working)
  // So basically, should we always call getInput() and then validate what the user is entering
  // /running or like... IDK
  public void run() {
    try {
      this.ui.identifyUser();
      getInput("[yYnNq]");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void validateInput(String regex, Scanner scan) {
    while (!scan.hasNext(regex)) {
      this.ui.printText("Sorry, please pick from given options");
      scan.next();
    }
  }

}