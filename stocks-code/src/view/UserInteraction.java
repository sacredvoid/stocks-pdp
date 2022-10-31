package view;

import java.util.List;
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

  public void getPortfolioNumber() {
    printText("Welcome! Please enter your portfolio number from the options: ");
  }

  public void prettyPrintPortfolios(String[] portfolio_list) {
    for (String file: portfolio_list
    ) {
      printText(file);
    }
  }
  public void printPortfolioData(String pfData) {
    // Write a portfolio parser
    String[] columns = pfData.split("\n");
    int rowCount = columns[0].split(",").length;
    String leftAlignF = "";

    for(int i=0;i<rowCount;i++) {
      System.out.print("+-------------");
    }
    System.out.print("+\n");
    if(rowCount == 2){
      leftAlignF = "| %-11s | %-11s | %n";
      System.out.format("| Stock Name  | Quantity    |%n");
      for(int i=0;i<rowCount;i++) {
        System.out.print("+-------------");
      }
      System.out.print("+\n");
      for (String column : columns) {
        String[] stockQtVal = column.split(",");
        System.out.format(leftAlignF, stockQtVal[0], stockQtVal[1]);
      }
      for(int i=0;i<rowCount;i++) {
        System.out.print("+-------------");
      }
      System.out.print("+\n");
    }
    else if(rowCount == 3) {
      leftAlignF = "| %-11s | %-11s | %-11s |%n";
      System.out.format("| Stock Name  | Quantity    | Value       |%n");
      for(int i=0;i<rowCount;i++) {
        System.out.print("+-------------");
      }
      System.out.print("+\n");
      for (String column : columns) {
        String[] stockQtVal = column.split(",");
        System.out.format(leftAlignF, stockQtVal[0], stockQtVal[1], stockQtVal[2]);
      }
      for(int i=0;i<rowCount;i++) {
        System.out.print("+-------------");
      }
      System.out.print("+\n");
    }
  }
}
