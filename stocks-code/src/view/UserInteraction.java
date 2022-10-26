package view;

import view.PrintHeader;

/**
 * Our "view" class which handles all showing stuff
 */

public class UserInteraction {

  public UserInteraction() {
    PrintHeader.printHeader();
  }

  public void identifyUser() {
    printText("Are you an existing customer?");
    printText("Enter 'Y' or 'y' for yes and 'N' or 'n' for no: ");
  }

  public void printFooter() {
    printText("Thank you for using our Stock Broking Platform!");
  }

  public void printText(String s) {
    System.out.println(s);
  }

}
