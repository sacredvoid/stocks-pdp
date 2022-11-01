import controller.Handler;
import controller.InteractionHandler;
import java.io.InputStreamReader;

import model.ModelOrchestrator;
import model.Orchestrator;
import view.UserInteraction;

public class ClientRunner {


  public static void main(String[] args) {

    // Model Initialization
    Orchestrator model = new ModelOrchestrator();

    // View Initialization
    UserInteraction viewer = new UserInteraction();

    Handler ih = new InteractionHandler(
        model,
        viewer,
        new InputStreamReader(System.in), System.out);
    ih.run();
  }
}