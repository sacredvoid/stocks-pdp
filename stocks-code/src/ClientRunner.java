import controller.InteractionHandler;
import java.io.InputStreamReader;
import view.UserInteraction;

public class ClientRunner {


  public static void main(String[] args) {

    // Model Initialization
    Object o = new Object();

    // View Initialization
    UserInteraction viewer = new UserInteraction();

    InteractionHandler ih = new InteractionHandler(
        o,
        viewer,
        new InputStreamReader(System.in), System.out);
    ih.run();
  }
}