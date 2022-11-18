package controller;

import controller.commands.CreatePortfolio;
import controller.commands.GetPortfolioComposition;
import controller.commands.GetPortfolioValue;
import controller.commands.IPortfolioCommands;
import controller.commands.LoadExternalPortfolio;
import controller.commands.ModifyPortfolio;
import controller.commands.PortfolioPerformance;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import model.Orchestrator;
import view.UserInteraction;

/**
 * The new Interaction handler v2 which was written keeping Command Design Pattern in mind and this
 * controller gets the user input, designates the commands from user to the respective functions and
 * passes information to view.
 */
public class InteractionHandlerV2 extends AbstractHandler {

  /**
   * The Model object.
   */
  Orchestrator morch;
  /**
   * The Accepted commands hashmap.
   */
  Map<String, Function<Scanner, IPortfolioCommands>> acceptedCommands = new HashMap<>();

  /**
   * Constructor class which takes a Readable input and Printstream output, which is used by our
   * view to display outputs from model to console.
   *
   * @param input Readable type input object
   * @param morch Prinstream type output object
   * @param ui    the ui
   */
  public InteractionHandlerV2(Readable input, Orchestrator morch, UserInteraction ui) {
    super.ui = ui;
    this.morch = morch;
    this.scan = new Scanner(input);
    this.initializeCommands();
  }

  private void initializeCommands() {
    // Command to set the commission fees
    acceptedCommands.put("0", s -> {
      this.ui.printText("By Default, we charge our users $1 per transaction (buy/sell).",
          "Y");
      this.ui.printText(
          "The new commission fees that you set, will be reset every time you quit the "
              + "application.", "Y");
      this.ui.printText(
          "Please enter the Commission Fees per transaction that you would like to set for this "
              + "time, or Q/q to quit", "Y");
      String commissionFees = this.getInput("[0-9]+|q|Q");
      if (!commissionFees.equalsIgnoreCase("q")) {
        if (commissionFees.equals("0")) {
          this.ui.printText("Sorry, cannot set commission fees to zero.", "R");
          return null;
        }
        this.morch.setCommissionFees(commissionFees);
        this.ui.printText(
            "Commission Fees for this application lifecycle was set to: " + commissionFees,
            "G");
      }
      return null;
    });
    // Command to take path to CSV and load it into our system
    acceptedCommands.put("1", s -> {
      this.ui.printText("Enter path to CSV:", "Y");
      String input = this.getInput("");
      return new LoadExternalPortfolio(input);
    });
    // Command to view a portfolio's composition
    acceptedCommands.put("2", s -> {
      // Using view to pull data from model using model-view
      this.ui.getExistingPortfolios();
      if (this.ui.getStatus().contains("Failed")) {
        return null;
      }
      this.ui.printText("Enter your portfolio ID:", "Y");
      String input = this.getInput(ValidateData.getRegex("portfolio"));
      return new GetPortfolioComposition(input);
    });
    // Command to edit a portfolio
    acceptedCommands.put("3", s -> {
      this.ui.getExistingPortfolios();
      if (this.ui.getStatus().contains("Failed")) {
        return null;
      }
      StringBuilder inputStockCalls = new StringBuilder();
      this.ui.printText("Enter Portfolio ID you want to edit shares for:", "Y");
      String pfID = this.getInput(ValidateData.getRegex("portfolio"));
      this.ui.printText("Enter STOCK,QUANTITY,DATE,CALL, q/Q to stop entering", "Y");
      this.ui.printText("Example: AAPL,20,2020-10-13,BUY", "G");
      String callRequests = getMultilineInput(inputStockCalls,
          ValidateData.getComplexRegex(new String[]{"stock", "quantity", "date", "call"})
              + "|" + ValidateData.getRegex("quit"),
          "modify");
      return new ModifyPortfolio(pfID, callRequests);
    });
    // Command to get the value for a portfolio
    acceptedCommands.put("4", s -> {
      this.ui.getExistingPortfolios();
      if (this.ui.getStatus().contains("Failed")) {
        return null;
      }
      this.ui.printText("Enter portfolio ID you want to get value for:", "Y");
      String pfID = this.getInput("");
      this.ui.printText("Enter date you want to see value for", "Y");
      String date = this.getInput(ValidateData.getRegex("date"));
      return new GetPortfolioValue(pfID, date);
    });
    // Command to create a new portfolio
    acceptedCommands.put("5", s -> {
      StringBuilder inputStockData = new StringBuilder();
      this.ui.printText("Enter STOCK, QUANTITY, DATE", "Y");
      this.ui.printText("Enter q/Q to stop entering", "Y");
      String inputData = getMultilineInput(inputStockData,
          ValidateData.getComplexRegex(new String[]{"stock", "quantity", "date"})
              + "|" + ValidateData.getRegex("quit"),
          "create");
      if (inputData.isEmpty()) {
        return null;
      }
      return new CreatePortfolio(inputData);
    });
    // Command to get performance for a given portfolio and date range
    acceptedCommands.put("6", s -> {
      this.ui.getExistingPortfolios();
      if (this.ui.getStatus().contains("Failed")) {
        return null;
      }
      this.ui.printText("Enter portfolio ID you want to view performance for:", "Y");
      String pfId = this.getInput(ValidateData.getRegex("portfolio"));
      this.ui.printText("Enter start date of the date range:", "Y");
      String startDate = this.getInput(ValidateData.getRegex("date"));
      this.ui.printText("Enter end date of the date range:", "Y");
      String endDate = this.getInput(ValidateData.getRegex("date"));
      return new PortfolioPerformance(pfId, startDate, endDate);
    });
    // Command to get cost-basis for a given portfolio
    acceptedCommands.put("7", s -> {
      this.ui.getExistingPortfolios();
      if (this.ui.getStatus().contains("Failed")) {
        return null;
      }
      this.ui.printText("Enter Portfolio ID you want to see Cost-Basis for", "Y");
      String pfId = this.getInput(ValidateData.getRegex("portfolio"));
      this.ui.printText("Enter the date that you want to see Cost-Basis evaluation for",
          "Y");
      String date = this.getInput(ValidateData.getRegex("date"));
      this.ui.printCostBasis(pfId, date);
      return null;
    });
  }

  /**
   * Helper method to take multiline inputs and append them to a string.
   *
   * @param inputData the stringbuilder object that we'll return to the command containing the data
   * @param regex     the regex to check the input with
   * @param caller    the indicator from where this method was called, defines behaviour
   * @return String containing the aggregated data
   */
  private String getMultilineInput(StringBuilder inputData, String regex, String caller) {
    while (true) {
      String data = this.getInput(regex);
      if (data.equalsIgnoreCase("q")) {
        break;
      }
      inputData.append(data);
      if (caller.equals("create")) {
        inputData.append(",BUY");
      }
      inputData.append("\n");
    }
    return inputData.toString();
  }

  @Override
  public void run() {
    IPortfolioCommands commandObject;
    // Write the new flow here
    while (true) {
      this.ui.printMenu();
      String input = scan.next();
      if (input.equalsIgnoreCase("q")) {
        return;
      }
      Function<Scanner, IPortfolioCommands> commandEntered = acceptedCommands.getOrDefault(
          input, null
      );

      if (commandEntered == null) {
        this.ui.printText("Command not recognized, retry.", "R");
      } else {
        commandObject = commandEntered.apply(scan);
        if (commandObject == null) {
          continue;
        }
        commandObject.runCommand(this.morch);
        this.ui.printText("Output:", "Y");
        String color = "";
        if (commandObject.getIsTabularDataBoolean()) {
          this.ui.printTabularData(commandObject.getStatusMessage());
        } else {
          if (commandObject.getStatusMessage().contains("Sorry")) {
            color = "R";
          } else {
            color = "G";
          }
          this.ui.printText(commandObject.getStatusMessage(), color);
        }
      }
    }
  }
}