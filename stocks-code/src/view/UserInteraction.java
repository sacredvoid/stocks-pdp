package view;

import java.io.ByteArrayOutputStream;
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
  private ByteArrayOutputStream outData;

  private String status;

  /**
   * Default constructor that takes in a PrintStream object and initializes the class attribute.
   *
   * @param out PrintStream object
   */
  public UserInteraction(PrintStream out, Orchestrator model) {
    this.outStream = out;
    this.modelView = new ModelView(model);
    this.outData = new ByteArrayOutputStream();
  }

  /**
   * Method that prints the welcome text for our application and some instruction for user to quit
   * application.
   */
  public void printHeader() {
    printText("Welcome To Aaka-Sam Stock Trading!", "G");
    printText("You can always quit the platform by pressing 'q'", "Y");
  }

  public ByteArrayOutputStream getOutData() {
    return this.outData;
  }

  public void setStatus(String statusMSG) {
    this.status = statusMSG;
  }

  public String getStatus() {
    return this.status;
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
    String[] existingPortfolios = this.modelView.getExistingPortfolios();
    if(existingPortfolios.length == 0) {
      printText("No Portfolios Found! You can create one though.","R");
      setStatus("Failed to load portfolios");
      return;
    }
    for (String file : existingPortfolios
    ) {
      printText(file, "");
    }
    setStatus("Portfolio view successful");
  }

  /**
   * Method that pretty prints the portfolio in a tabular form, is dynamic depending on the number
   * of columns that the input data string has.
   *
   * @param pfData portfolio data as string
   */
  public void printTabularData(String pfData) {
    // Write a portfolio parser
    String[] columns = pfData.split("\n");
    if(columns.length == 1) {
      printText(columns[0],"R");
      return;
    }
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
      this.outStream.format("| Stock Name      | Quantity        | Value($)        |%n");
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

  public void printCostBasis(String pfID, String date) {
    String[] costBasisInfo = this.modelView.getCostBasis(pfID, date);
    if(costBasisInfo.length == 1) {
      printText(costBasisInfo[0],"R");
      return;
    }
    printText("Total Amount Invested    : $"+costBasisInfo[0],"Y");
    printText("Total Commission Charged : $"+costBasisInfo[1],"R");
    printText("Total Amount+Commission  : $"+costBasisInfo[3],"Y");
    printText("Total Earned by Selling  : $"+costBasisInfo[2],"G");
  }

  public void printMenu() {
    this.printText("Choose from the following:","G");
    this.printText("0 - Set Commission Fees","Y");
    this.printText("1 - Load External Portfolio","Y");
    this.printText("2 - View Portfolio Composition","Y");
    this.printText("3 - Modify existing Portfolio","Y");
    this.printText("4 - Get Portfolio Value","Y");
    this.printText("5 - Create New Portfolio","Y");
    this.printText("6 - View Portfolio Performance","Y");
    this.printText("7 - Get Cost-Basis for Portfolio and Date","Y");
    this.printText("q/Q - Quit Application","Y");
  }
}
