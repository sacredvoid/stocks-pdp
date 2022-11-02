package controller;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Scanner;

import model.ModelOrchestrator;
import model.Orchestrator;
import view.UserInteraction;

/**
 * Controller Class which accepts input from the terminal and decides what to do accordingly. This
 * class is the bridge between the model and view and defines our Stock Platform's logical flow.
 */
public class InteractionHandler implements Handler {

  private final UserInteraction ui;
  private Scanner scan;

  private Orchestrator modelOrch;
  private final String portfolioRegex = "q|Q|[0-9]{6}";
  private static final String SCRIP_REGEX = "q|Q|([A-za-z0-9])+([.]([A-za-z])+)?";
  private static final String QUANTITY_REGEX = "[0-9]+";

  private static final String VALID_DATE_REGEX =
      "q|Q|(19|20)[0-9]{2}-[0-9]{2}-[0-9]{2}";


  /**
   * Constructor class which takes a Readable input and Printstream output, which is used by our
   * view to display outputs from model to console.
   *
   * @param input  Readable type input object
   * @param output Prinstream type output object
   */
  public InteractionHandler(Readable input, PrintStream output) {
    this.modelOrch = new ModelOrchestrator();
    this.ui = new UserInteraction(output);
    this.scan = new Scanner(input);
  }

  /**
   * Function to accept inputs and returns the input from console as a String. Takes a Regular
   * Expression as parameter which is used to check and accept/reject input from console as per
   * application requirements.
   *
   * @param regex a string which describes the input pattern to match
   * @return a string with the validated input from console
   */
  private String getInput(String regex) {
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
  private void validateInput(String regex, Scanner scan) {
    while (!scan.hasNext(regex)) {
      this.ui.printText("Sorry, input did not match requirements!", "R");
      scan.next();
    }
  }

  /**
   * Our main method which takes control from client and runs the logical flow of our application
   * like loading, creating and fetching values of portfolios.
   */
  @Override
  public void run() {
    this.ui.printHeader();
    // Get the inputs and decide the flow
    String input = "";
    // Just naming the loop to break to
    mainrunner:
    while (!input.equalsIgnoreCase("q")) {
      this.ui.printText("Select from '1/2/3':", "Y");
      this.ui.printText("1. Load External Portfolio", "Y");
      this.ui.printText("2. Access existing Portfolio", "Y");
      this.ui.printText("3. Create new Portfolio", "Y");
      input = getInput("q|Q|1|2|3");
      if (input.equalsIgnoreCase("q")) {
        break;
      }
      if (input.equals("1")) {
        // User trying to load an external CSV
        this.ui.printText("Please provide the path to load a CSV", "Y");
        input = getInput("");
        if (input.equalsIgnoreCase("q")) {
          break;
        }
        String message = "";
        try {
          message = this.modelOrch.loadExternalCSV(input);
          if (message.contains("Not")) {
            this.ui.printText(message, "R");
          } else {
            this.ui.printText("File read successful: " + message + ".csv", "G");
          }
        } catch (FileNotFoundException f) {
          this.ui.printText("File not found, please enter a correct path", "R");
        }
        this.ui.printText("File read successful. Portfolio ID: " + message, "G");
      } else if (input.equals("2")) {
        // User trying to access an existing portfolio
        if (this.modelOrch.showExistingPortfolios() != null) {
          this.ui.getPortfolioNumber();
          this.ui.printText("Pick from existing portfolios:", "Y");
          this.ui.prettyPrintPortfolios(this.modelOrch.showExistingPortfolios());
        } else {
          this.ui.printText("Sorry, no existing portfolios found!", "R");
          continue;
        }
        input = getInput(portfolioRegex);
        if (input.equalsIgnoreCase("q")) {
          break;
        }
        this.ui.printText("Your portfolio number:" + input, "G");
        try {
          String pfData = this.modelOrch.getPortfolio(input);
          if (!pfData.isEmpty()) {
            this.ui.printText("Here's your data!", "Y");
            this.ui.printPortfolioData(pfData);
            dateLoop:
            while (!input.equalsIgnoreCase("b")) {
              this.ui.printText(
                  "Check portfolio value on a given date (YYYY-MM-DD) [avoid weekends] "
                      + "or exit:'b/B':", "Y");
              input = getInput("B|b|" + VALID_DATE_REGEX);
              if (input.equalsIgnoreCase("q")) {
                break mainrunner;
              }
              if (!input.equalsIgnoreCase("b")) {
                try {
                  String pfValue = this.modelOrch.getPortfolioValue(input, pfData);
                  if (pfValue != null) {
                    this.ui.printPortfolioData(pfValue);
                  } else {
                    this.ui.printText(
                        "Sorry, the date entered is either a "
                            + "weekend or a future date, please re-enter:",
                        "Y");
                  }
                } catch (ParseException e) {
                  this.ui.printText("Couldn't parse text!", "R");
                }
              } else {
                this.ui.printFooter();
              }
            }

          } else {
            this.ui.printText(
                "Nothing found in your portfolio file. Please try again!", "Y"
            );
          }
        } catch (FileNotFoundException f) {
          this.ui.printText(String.format(
              "Given portfolio ID: %s doesn't exist!", input), "R"
          );
        }
      } else {
        // New user, needs to make a portfolio!
        this.ui.printText("Welcome to our platform!", "G");
        input = "";
        StringBuilder stockData = new StringBuilder();
        while (!input.equalsIgnoreCase("f")) {
          this.ui.printText("Enter 'F/f' to finish entering stock details", "Y");
          this.ui.printText("Please enter the details like so: Stock,Quantity:", "Y");
          this.ui.printText("Example: AAPL,20", "Y");
          stockData.append(input).append("\n");
          this.ui.printText(stockData.toString(), "");
          input = getInput("F|f|" + SCRIP_REGEX + "," + QUANTITY_REGEX);
          if (input.equalsIgnoreCase("q")) {
            break mainrunner;
          }
        }
        String message = "";
        if (!stockData.toString().equals("\n")) {
          message = this.modelOrch.createPortfolio(stockData.toString());
        } else {
          message = "No data entered, exiting...";
        }
        this.ui.printText(message, "");
        this.ui.printFooter();
      }
    }
  }

}