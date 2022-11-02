import controller.Handler;
import controller.InteractionHandler;
import java.io.InputStreamReader;
public class ClientRunner {


  public static void main(String[] args) {
    Handler ih = new InteractionHandler(new InputStreamReader(System.in), System.out);
    ih.run();
  }
}