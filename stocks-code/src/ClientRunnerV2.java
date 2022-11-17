import controller.Handler;
import controller.InteractionHandlerV2;
import java.io.InputStreamReader;
import model.ModelOrchestratorV2;
import model.Orchestrator;
import view.UserInteraction;

/**
 * The client runner class which calls our controller and thus runs the application.
 */
public class ClientRunnerV2 {

  /**
   * Initializes the interaction handler class and sends in the input/output classes.
   *
   * @param args none
   */

  public static void main(String[] args) {
    Orchestrator morch = new ModelOrchestratorV2();
    UserInteraction ui = new UserInteraction(System.out,morch);
    Handler ih = new InteractionHandlerV2(new InputStreamReader(System.in), morch, ui);
    ih.run();
  }
}