package controller;

import controller.commands.CreatePortfolio;
import controller.commands.GetPortfolioComposition;
import controller.commands.GetPortfolioValue;
import controller.commands.LoadExternalPortfolio;
import controller.commands.ModifyPortfolio;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import model.ModelOrchestratorV2;
import model.Orchestrator;
import view.UserInteraction;

public class InteractionHandlerV2 extends AbstractHandler {

  UserInteraction ui;
  Orchestrator morch;
  Map<String, Function<Scanner, IPortfolioCommands>> acceptedCommands = new HashMap<>();
  /**
   * Constructor class which takes a Readable input and Printstream output, which is used by our
   * view to display outputs from model to console.
   *
   * @param input  Readable type input object
   * @param morch Prinstream type output object
   */
  public InteractionHandlerV2(Readable input, Orchestrator morch, UserInteraction ui) {
    this.ui = ui;
    this.morch = morch;
    this.scan = new Scanner(input);
    this.initializeCommands();
  }

  private void initializeCommands() {
    acceptedCommands.put("1",s-> {
      this.ui.printText("Enter path to JSON:","Y");
      String input = this.getInput("");
      return new LoadExternalPortfolio(input);
    });
    acceptedCommands.put("2",s-> {
      // Using view to pull data from model using model-view
      this.ui.getExistingPortfolios();
      this.ui.printText("Enter your portfolio ID:","Y");
      String input = this.getInput("");
      return new GetPortfolioComposition(input);
    });
    acceptedCommands.put("3",s-> {
      this.ui.getExistingPortfolios();
      StringBuilder inputStockCalls = new StringBuilder();
      this.ui.printText("Enter Portfolio ID you want to edit shares for:","Y");
      String pfID = this.getInput("");
      this.ui.printText("Enter STOCK,QUANTITY,DATE,CALL","Y");
      this.ui.printText("Example: AAPL,20,2020-10-13,BUY","G");
      String callRequests = getMultilineInput(inputStockCalls,"","modify");
      return new ModifyPortfolio(pfID, callRequests);
    });
    acceptedCommands.put("4",s-> {
      this.ui.getExistingPortfolios();
      this.ui.printText("Enter portfolio ID you want to get value for:","Y");
      String pfID = this.getInput("");
      this.ui.printText("Enter date you want to see value for","Y");
      String date = this.getInput("");
      return new GetPortfolioValue(pfID,date);
    });
    acceptedCommands.put("5",s-> {
      StringBuilder inputStockData = new StringBuilder();
      this.ui.printText("Enter STOCK, QUANTITY, DATE","Y");
      this.ui.printText("Enter q/Q to stop entering","Y");
      String inputData = getMultilineInput(inputStockData,"","create");
      if(inputData.equals("")){
        return new CreatePortfolio("no data provided");
      }
      return new CreatePortfolio(inputStockData.toString());
    });
  }

  private String getMultilineInput(StringBuilder inputData, String regex, String caller) {
    while (true) {
      String data = this.getInput(regex);
      if(data.equalsIgnoreCase("q")) {
        break;
      }
      inputData.append(data);
      if(caller.equals("create")) {
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
      if(input.equalsIgnoreCase("q")) {
        return;
      }
      Function<Scanner, IPortfolioCommands> commandEntered = acceptedCommands.getOrDefault(
          input, null
      );

      if(commandEntered == null) {
        this.ui.printText("Command not recognized, retry.","R");
      }
      else {
        commandObject = commandEntered.apply(scan);
        commandObject.go(this.morch);
        this.ui.printText("Output:","Y");
        this.ui.printText(commandObject.getStatusMessage(),"G");
      }
    }
  }
}