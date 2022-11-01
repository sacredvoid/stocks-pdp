package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import model.ModelOrchestrator;
import model.Orchestrator;
import view.UserInteraction;

/**
 * Controller Class which accepts input from the terminal and decides what to do accordingly.
 * This class talks to the Runner class which is the main program orchestrator.
 */
public class InteractionHandler implements Handler{
  private final Readable in;
  private final Appendable out;
  private final UserInteraction ui;
  private Scanner scan;

  private final String yesNoRegex = "[YyNnqQ]";
  private final String portfolioRegex = "q|Q|[0-9]{6}";
  private static final String SCRIP_REGEX = "q|Q|([A-za-z0-9])+([.]([A-za-z])+)?";
  private static final String QUANTITY_REGEX = "[0-9]+";

  private static final String VALID_DATE_REGEX =
      "q|Q|(19|20)[0-9]{2}-[0-9]{2}-[0-9]{2}";

  private Orchestrator modelOrch;

  public InteractionHandler(Orchestrator model, UserInteraction ui, Readable input, Appendable output) {
    this.in = input;
    this.out = output;
    this.modelOrch = model;
    this.ui = ui;
    this.scan = new Scanner(this.in);
  }

  private String getInput(String regex) {
    if (!regex.isEmpty()) {
      validateInput(regex, scan);
    }
    return scan.next();
  }

  private void validateInput(String regex, Scanner scan) {
    while (!scan.hasNext(regex)) {
      this.ui.printText("Sorry, input did not match requirements!","R");
      scan.next();
    }
  }


  public String getOutput() {
    return this.out.toString();
  }

  @Override
  public void run() {
    // Get the inputs and decide the flow
    String input = "";
    // Just naming the loop to break to
    mainrunner:
    while (!input.equalsIgnoreCase("q")) {
      this.ui.printText("Select from '1/2/3':","Y");
      this.ui.printText("1. Load External Portfolio","Y");
      this.ui.printText("2. Access existing Portfolio","Y");
      this.ui.printText("3. Create new Portfolio","Y");
      input = getInput("q|Q|1|2|3");
      if (input.equalsIgnoreCase("q")) {
        break;
      }
      if (input.equals("1")) {
        this.ui.printText("Please provide the path to load a CSV","Y");
        input = getInput("");
        if (input.equalsIgnoreCase("q")) {
          break;
        }
        String message ="";
        try {
          message = this.modelOrch.loadExternalCSV(input);
        }
        catch (FileNotFoundException f) {
          this.ui.printText("File not found, please enter a correct path","R");
        }
        this.ui.printText("File read successful: "+message+".csv","G");

      } else if(input.equals("2")) {
          if (this.modelOrch.showExistingPortfolios() != null) {
            this.ui.getPortfolioNumber();
            this.ui.printText("Pick from existing portfolios:","Y");
            this.ui.prettyPrintPortfolios(this.modelOrch.showExistingPortfolios());
          } else {
            this.ui.printText("Sorry, no existing portfolios found!","R");
            continue;
          }
          input = getInput(portfolioRegex);
          if (input.equalsIgnoreCase("q")) break;
          this.ui.printText("Your portfolio number:" + input,"G");
          try {
            String pfData = this.modelOrch.getPortfolio(input);
            if (!pfData.isEmpty()) {
              this.ui.printText("Here's your data!","Y");
              this.ui.printPortfolioData(pfData);
              dateLoop:
              while (!input.equalsIgnoreCase("b")) {
                this.ui.printText("Check portfolio value on a given date (YYYY-MM-DD) [avoid weekends] or exit:'b/B':","Y");
                input = getInput("B|b|" + VALID_DATE_REGEX);
                if (input.equalsIgnoreCase("q")) break mainrunner;
                if (!input.equalsIgnoreCase("b")) {
                  try {
                    String pfValue = this.modelOrch.getPortfolioValue(input, pfData);
                    if (!(pfValue == null)) {
                      this.ui.printPortfolioData(pfValue);
                    } else {
                      this.ui.printText("Sorry, the date entered is a weekend, please re-enter:","Y");
                    }
                  } catch (ParseException e) {
                    this.ui.printText("Couldn't parse text!","R");
                  }
                } else {
                  this.ui.printFooter();
                }
              }

            } else this.ui.printText("Nothing found in your portfolio file. Please try again!","Y");
          } catch (FileNotFoundException f) {
            this.ui.printText(String.format("Given portfolio ID: %s doesn't exist!", input),"R");
          }
        } else {
          // New user, needs to make a portfolio!
          this.ui.printText("Welcome to our platform!","G");
            input = "";
            StringBuilder stockData = new StringBuilder();
            while (!input.equalsIgnoreCase("f")) {
              this.ui.printText("Enter 'F/f' to finish entering stock details","Y");
              this.ui.printText("Please enter the details like so: Stock,Quantity:","Y");
              this.ui.printText("Example: AAPL,20","Y");
              stockData.append(input).append("\n");
              this.ui.printText(stockData.toString(),"");
              input = getInput("F|f|" + SCRIP_REGEX + "," + QUANTITY_REGEX);
              if (input.equalsIgnoreCase("q")) break mainrunner;
            }
            String message = "";
            if (!stockData.toString().equals("\n")) {
              message = this.modelOrch.createPortfolio(stockData.toString());
            }
            else {
              message = "No data entered, exiting...";
            }
            this.ui.printText(message,"");
            this.ui.printFooter();
        }
      }
    }

}