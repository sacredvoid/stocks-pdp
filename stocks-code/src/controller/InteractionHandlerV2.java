package controller;

import controller.commands.CreatePortfolio;
import controller.commands.GetPortfolioComposition;
import controller.commands.GetPortfolioValue;
import controller.commands.LoadExternalPortfolio;
import controller.commands.ModifyPortfolio;
import controller.commands.PortfolioPerformance;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import model.ModelOrchestratorV2;
import view.UserInteraction;

public class InteractionHandlerV2 extends AbstractHandler {

  Map<String, Function<Scanner, IPortfolioCommands>> acceptedCommands = new HashMap<>();

  /**
   * Constructor class which takes a Readable input and Printstream output, which is used by our
   * view to display outputs from model to console.
   *
   * @param input  Readable type input object
   * @param output Prinstream type output object
   */
  public InteractionHandlerV2(Readable input, PrintStream output) {
    this.modelOrch = new ModelOrchestratorV2();
    this.ui = new UserInteraction(output, this.modelOrch);
    this.scan = new Scanner(input);
    this.initializeCommands();
  }

  private void initializeCommands() {
    acceptedCommands.put("1", s -> {
      this.ui.printText("Enter path to JSON:", "Y");
      String input = this.getInput("");
      return new LoadExternalPortfolio(input);
    });
    acceptedCommands.put("2", s -> {
      // Using view to pull data from model using model-view
      this.ui.getExistingPortfolios();
      this.ui.printText("Enter your portfolio ID:", "Y");
      String input = this.getInput("");
      return new GetPortfolioComposition(input);
    });
    acceptedCommands.put("3", s -> {
      this.ui.printText("Enter Portfolio ID you want to edit shares for:", "Y");
      String pfID = this.getInput("");
      this.ui.printText("Enter STOCK,QUANTITY,DATE,CALL", "Y");
      this.ui.printText("Example: AAPL,20,2020-10-13,BUY", "G");
      String stockData = this.getInput("");
      return new ModifyPortfolio(pfID, stockData);
    });
    acceptedCommands.put("4", s -> {
      this.ui.getExistingPortfolios();
      this.ui.printText("Enter portfolio ID you want to get value for:", "Y");
      String pfID = this.getInput("");
      this.ui.printText("Enter date you want to see value for", "Y");
      String date = this.getInput("");
      return new GetPortfolioValue(pfID, date);
    });
    acceptedCommands.put("5", s -> {
      StringBuilder inputStockData = new StringBuilder();
      this.ui.printText("Enter STOCK, QUANTITY, DATE", "Y");
      this.ui.printText("Enter q/Q to stop entering", "Y");
      while (true) {
        String data = this.getInput("");
        if (data.equalsIgnoreCase("q")) {
          if (inputStockData.toString().equals("")) {
            return new CreatePortfolio("no data provided");
          }
          break;
        } else {
          inputStockData.append(data).append("\n");
//          return new CreatePortfolio(inputStockData.toString());
        }
      }
      return new CreatePortfolio(inputStockData.toString());
    });
    acceptedCommands.put("6", s -> {
      this.ui.getExistingPortfolios();
      this.ui.printText("Enter portfolio ID you want to view performance for:", "Y");
      String pfId = this.getInput("");
      this.ui.printText("Enter start date of the date range:", "Y");
      String startDate = this.getInput("");
      this.ui.printText("Enter end date of the date range:", "Y");
      String endDate = this.getInput("");
      return new PortfolioPerformance(pfId, startDate, endDate);
    });
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
        commandObject.go(this.modelOrch);
        this.ui.printText("Output:", "Y");
        this.ui.printText(commandObject.getStatusMessage(), "G");
      }
    }
  }
}