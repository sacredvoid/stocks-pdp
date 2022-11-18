package controller;

import java.util.Scanner;
import view.UserInteraction;

/**
 * This class defines some common method that every controller on our platform needs to implement.
 * This contains methods like getInput and validateInput.
 */
public abstract class AbstractHandler implements Handler {

  protected Scanner scan;
  protected UserInteraction ui;

  /**
   * Function to accept inputs and returns the input from console as a String. Takes a Regular
   * Expression as parameter which is used to check and accept/reject input from console as per
   * application requirements.
   *
   * @param regex a string which describes the input pattern to match
   * @return a string with the validated input from console
   */
  protected String getInput(String regex) {
    if (!regex.isEmpty()) {
      validateInput(regex, scan);
    }
    return scan.next();
  }


  /**
   * Function which checks if input from user matches our specifications or not, like pre-defined
   * inputs, Stock Ticker Regex and Quantity regex.
   *
   * @param regex a string which describes the input pattern to match
   * @param scan  an Object of type Scanner which is used to check console input with
   *              scan.hasNext()
   */
  protected void validateInput(String regex, Scanner scan) {
    while (!scan.hasNext(regex)) {
      this.ui.printText("Sorry, input did not match requirements!", "R");
      scan.next();
    }
  }

}
