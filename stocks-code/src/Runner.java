import java.io.IOException;
import java.io.InputStreamReader;

public class Runner {


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
