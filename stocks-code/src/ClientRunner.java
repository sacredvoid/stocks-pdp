import controller.Handler;
import controller.InteractionHandler;
import java.io.InputStreamReader;

import model.ModelOrchestrator;
import model.Orchestrator;
import view.UserInteraction;

public class ClientRunner {


  public static void main(String[] args) {
    Handler ih = new InteractionHandler(new InputStreamReader(System.in), System.out);
    ih.run();
  }
}