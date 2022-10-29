package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
  private final UserInteraction ui;
  private Scanner scan;

  private final String yesNoRegex = "[YyNn]";
  private final String portfolioRegex = "[0-9]{6}";
  private static final String SCRIP_REGEX = "[A-Z]{3,5}";
  private static final String QUANTITY_REGEX = "[0-9]+";

  private static final String VALID_DATE_REGEX =
      "(19|20)[0-9]{2}-[0-9]{2}-[0-9]{2}";

  private PortfolioHandler pfH = new PortfolioHandler();

  public InteractionHandler(Object model, UserInteraction ui, Readable input, Appendable output) {
    this.in = input;
    this.out = output;
    this.model = model;
    this.ui = ui;
    this.scan = new Scanner(this.in);
  }

  public String getInput(String regex) {
    validateInput(regex, scan);
    return scan.next();
  }

  public String getOutput() {
    return this.out.toString();
  }

  public void run() {
      // Get the inputs and decide the flow

    while (true) {
      String input;
      this.ui.identifyUser();
      input = getInput(yesNoRegex);
      if(input.equals("q")) {
        break;
      }
      if(input.equalsIgnoreCase("y")) {
        this.ui.getPortfolioNumber();
        this.ui.printText("Pick from existing portfolios:");
        this.ui.prettyPrintPortfolios(this.pfH.showExistingPortfolios());
        input = getInput(portfolioRegex);
        this.ui.printText("Your portfolio number:"+input);
        try{
          String pfData = this.pfH.getPortfolio(input);
          if(!pfData.isEmpty()) {
            this.ui.printText("Here's your data!");
            this.ui.printPortfolioData(pfData);
    // Pretty print using tables :
    // https://stackoverflow.com/questions/15215326/how-can-i-create-table-using-ascii-in-a-console
            while (!input.equals("b"))
            {
              this.ui.printText("Check portfolio value on a given date (YYYY-MM-DD) or exit:'b':");
              input = getInput("b|"+VALID_DATE_REGEX);
              try {
                this.ui.printPortfolioData(this.pfH.getPortfolioValue(input,pfData));
              }
              catch (ParseException e){
                this.ui.printText("Couldn't parse text!");
              }
            }

          }
          else this.ui.printText("Nothing found in your portfolio file. Please try again!");
        }
        catch (FileNotFoundException f) {
          this.ui.printText(String.format("Given portfolio ID: %s doesn't exist!",input));
        }
      }
      else {
        // New user, needs to make a portfolio!
        this.ui.printText("Welcome to our platform, would you like to create a new Portfolio?"
            + "Y/y/N/n");
        input = getInput(yesNoRegex);
        if(input.equalsIgnoreCase("y")) {
          input = "";
          StringBuilder stockData = new StringBuilder();
          while(!input.equals("f")) {
            this.ui.printText("Enter 'f' to finish entering stock details");
            this.ui.printText("Please enter the details like so: Stock,Quantity:");
            this.ui.printText("Example: AAPL,20");
            stockData.append(input).append("\n");
            this.ui.printText(stockData.toString());
            input = getInput("f|"+SCRIP_REGEX + "," + QUANTITY_REGEX);
          }
          String message = this.pfH.createPortfolio(stockData.toString());
          this.ui.printText(message);
        }
      }
    }
  }



  private void validateInput(String regex, Scanner scan) {
    while (!scan.hasNext(regex)) {
      this.ui.printText("Sorry, input did not match requirements!");
      scan.next();
    }
  }

}