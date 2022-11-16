package view;

import java.io.PrintStream;
import model.Orchestrator;
import modelview.ModelView;

/**
 * Our ViewHandler implementation which shows the outputs from our model to the user via
 * PrintStream.
 */

public class UserInteraction implements ViewHandler {

  private final String yellow = "\u001B[33m";
  private final String red = "\u001B[31m";
  private final String green = "\u001B[32m";
  private final String resetColor = "\u001B[0m";

  private PrintStream outStream;
  private ModelView modelView;

  /**
   * Default constructor that takes in a PrintStream object and initializes the class attribute.
   *
   * @param out PrintStream object
   */
  public UserInteraction(PrintStream out, Orchestrator model) {
    this.outStream = out;
    this.modelView = new ModelView(model);
  }

  /**
   * Method that prints the welcome text for our application and some instruction for user to quit
   * application.
   */
  public void printHeader() {
    printText("Welcome To Aaka-Sam Stock Trading!", "G");
    printText("You can always quit the platform by pressing 'q'", "Y");
  }

  /**
   * Method that prints the text at the end of the application.
   */
  public void printFooter() {
    printText("Thank you for using Aaka-Sam Trading.", "G");
    printText("Stay tuned for more features. :) \n", "G");
  }

  /**
   * Method that takes in a string s and string color to view it in the console.
   *
   * @param s     to print/attach to PrintStream object
   * @param color the first letter of a color, currently supports only "R"ed,"G"reen,"Y"ellow
   */
  public void printText(String s, String color) {
    if (color.equals("R")) {
      this.outStream.print(red + s + resetColor);
    } else if (color.equals("G")) {
      this.outStream.print(green + s + resetColor);
    } else if (color.equals("Y")) {
      this.outStream.print(yellow + s + resetColor);
    } else {
      this.outStream.print(s);
    }
    this.outStream.println();
  }

  /**
   * Method that requests user to enter portfolio number.
   */
  public void getPortfolioNumber() {
    printText("Welcome! Please enter your portfolio number from the options: ", "G");
  }

  /**
   * Method that takes in a list of strings (portfolio data) and prints it neatly.
   *
   */
  public void getExistingPortfolios() {
    for (String file : this.modelView.getExistingPortfolios()
    ) {
      printText(file, "");
    }
  }

  /**
   * Method that pretty prints the portfolio in a tabular form, is dynamic depending on the number
   * of columns that the input data string has.
   *
   * @param pfData portfolio data as string
   */
  public void printPortfolioData(String pfData) {
    // Write a portfolio parser
    String[] columns = pfData.split("\n");
    int rowCount = columns[0].split(",").length;
    String leftAlignF = "";

    if (rowCount == 2) {
      for (int i = 0; i < rowCount; i++) {
        this.outStream.print("+-------------");
      }
      this.outStream.print("+\n");
      leftAlignF = "| %-11s | %-11s |%n";
      this.outStream.format("| Stock Name  | Quantity    |%n");
      for (int i = 0; i < rowCount; i++) {
        this.outStream.print("+-------------");
      }
      this.outStream.print("+\n");
      for (String column : columns) {
        String[] stockQtVal = column.split(",");
        this.outStream.format(leftAlignF, stockQtVal[0], stockQtVal[1]);
      }
      for (int i = 0; i < rowCount; i++) {
        this.outStream.print("+-------------");
      }
      this.outStream.print("+\n");
    } else if (rowCount == 3) {
      for (int i = 0; i < rowCount; i++) {
        this.outStream.print("+-----------------");
      }
      this.outStream.print("+\n");
      leftAlignF = "| %-15s | %-15s | %-15s |%n";
      this.outStream.format("| Stock Name      | Quantity        | Value           |%n");
      for (int i = 0; i < rowCount; i++) {
        this.outStream.print("+-----------------");
      }
      this.outStream.print("+\n");
      for (String column : columns) {
        String[] stockQtVal = column.split(",");
        this.outStream.format(leftAlignF, stockQtVal[0], stockQtVal[1], stockQtVal[2]);
      }
      for (int i = 0; i < rowCount; i++) {
        this.outStream.print("+-----------------");
      }
      this.outStream.print("+\n");
    }
  }

  public void printMenu() {
    this.printText("Choose from the following:","G");
    this.printText("1 - Load External Portfolio","Y");
    this.printText("2 - View Portfolio Composition","Y");
    this.printText("3 - Modify existing Portfolio","Y");
    this.printText("4 - Get Portfolio Value","Y");
    this.printText("5 - Create New Portfolio","Y");
    this.printText("6 - View Portfolio Performance","Y");
    this.printText("q/Q - Quit Application","Y");
  }
}
