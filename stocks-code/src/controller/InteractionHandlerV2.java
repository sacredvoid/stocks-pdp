package controller;

import controller.commands.CreatePortfolio;
import controller.commands.GetPortfolioValue;
import controller.commands.LoadExternalPortfolio;
import controller.commands.ModifyPortfolio;
import controller.commands.ViewPortfolio;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import model.ModelOrchestrator;
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
    this.modelOrch = new ModelOrchestrator();
    this.ui = new UserInteraction(output);
    this.scan = new Scanner(input);
    this.initializeCommands();
  }

  private void initializeCommands() {
    acceptedCommands.put("1",s-> new LoadExternalPortfolio(s.next()));
    acceptedCommands.put("2",s-> new ViewPortfolio());
    acceptedCommands.put("3",s-> new ModifyPortfolio());
    acceptedCommands.put("4",s-> new GetPortfolioValue());
    acceptedCommands.put("5",s-> new CreatePortfolio());
  }

  @Override
  public void run() {
    IPortfolioCommands commandObject;
    // Write the new flow here
    while (scan.hasNext()) {
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
        commandObject.go(this.modelOrch);
        this.ui.printText("Executed the command!","G");
      }
    }

  }
}