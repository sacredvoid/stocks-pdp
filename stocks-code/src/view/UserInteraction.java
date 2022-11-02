package view;

import java.io.PrintStream;

/**
 * Our "view" class which handles all showing stuff
 */

public class UserInteraction implements ViewHandler{

  private final String yellow = "\u001B[33m";
  private final String red = "\u001B[31m";
  private final String green = "\u001B[32m";
  private final String resetColor = "\u001B[0m";

  private PrintStream outStream;

  public UserInteraction(PrintStream out) {
    this.outStream = out;
  }

  public void printHeader() {
    printText("Welcome To Aaka-Sam Stock Trading!","G");
    printText("You can always quit the platform by pressing 'q'","Y");
  }

  public void printFooter() {
    printText("Thank you for using Aaka-Sam Trading.","G");
    printText("Stay tuned for more features. :) \n","G");
  }

  public void printText(String s, String color) {
    if(color.equals("R")) {
      this.outStream.print(red+s+resetColor);
    }
    else if(color.equals("G")) {
      this.outStream.print(green+s+resetColor);
    }
    else if(color.equals("Y")) {
      this.outStream.print(yellow+s+resetColor);
    }
    else {
      this.outStream.print(s);
    }
    this.outStream.println();
  }

  public void getPortfolioNumber() {
    printText("Welcome! Please enter your portfolio number from the options: ","G");
  }

  public void prettyPrintPortfolios(String[] portfolio_list) {
    for (String file: portfolio_list
    ) {
      printText(file,"");
    }
  }
  public void printPortfolioData(String pfData) {
    // Write a portfolio parser
    String[] columns = pfData.split("\n");
    int rowCount = columns[0].split(",").length;
    String leftAlignF = "";

    for(int i=0;i<rowCount;i++) {
      this.outStream.print("+-------------");
    }
    this.outStream.print("+\n");
    if(rowCount == 2){
      leftAlignF = "| %-11s | %-11s | %n";
      this.outStream.format("| Stock Name  | Quantity    |%n");
      for(int i=0;i<rowCount;i++) {
        this.outStream.print("+-------------");
      }
      this.outStream.print("+\n");
      for (String column : columns) {
        String[] stockQtVal = column.split(",");
        this.outStream.format(leftAlignF, stockQtVal[0], stockQtVal[1]);
      }
      for(int i=0;i<rowCount;i++) {
        this.outStream.print("+-------------");
      }
      this.outStream.print("+\n");
    }
    else if(rowCount == 3) {
      leftAlignF = "| %-11s | %-11s | %-11s |%n";
      this.outStream.format("| Stock Name  | Quantity    | Value       |%n");
      for(int i=0;i<rowCount;i++) {
        this.outStream.print("+-------------");
      }
      this.outStream.print("+\n");
      for (String column : columns) {
        String[] stockQtVal = column.split(",");
        this.outStream.format(leftAlignF, stockQtVal[0], stockQtVal[1], stockQtVal[2]);
      }
      for(int i=0;i<rowCount;i++) {
        this.outStream.print("+-------------");
      }
      this.outStream.print("+\n");
    }
  }
}
