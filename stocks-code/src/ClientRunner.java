import controller.Handler;
import controller.InteractionHandler;
import java.io.InputStreamReader;

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
    Handler ih = new InteractionHandler(new InputStreamReader(System.in), System.out);
    ih.run();
  }
}