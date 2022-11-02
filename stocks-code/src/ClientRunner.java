import controller.Handler;
import controller.InteractionHandler;
import java.io.InputStreamReader;

/**
 * The client runner class.
 */
public class ClientRunner {

  /**
   * main run function
   *
   * @param args woohoo
   */

  public static void main(String[] args) {
    Handler ih = new InteractionHandler(new InputStreamReader(System.in), System.out);
    ih.run();
  }
}