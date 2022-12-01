import controller.Handler;
import controller.MainController;
import controller.TextUIHandler;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import model.ModelOrchestratorV2;
import model.Orchestrator;
import view.UserInteraction;

/**
 * The client runner class which calls our controller and thus runs the application.
 */
public class ClientRunner {

  /**
   * Initializes the interaction handler class and sends in the input/output classes.
   *
   * @param args none
   */

  public static void main(String[] args) {
    Orchestrator morch = new ModelOrchestratorV2<>();
    UserInteraction ui = new UserInteraction(System.out,morch);
    Handler ih = new MainController(new InputStreamReader(System.in), morch, ui);
    ih.run();
  }
}