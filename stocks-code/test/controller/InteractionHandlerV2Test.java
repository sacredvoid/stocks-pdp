package controller;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.Scanner;
import model.ModelOrchestratorV2;
import model.Orchestrator;
import org.junit.Before;
import org.junit.Test;
import view.UserInteraction;
import view.ViewHandler;

public class InteractionHandlerV2Test {

  Handler ih;

  private final String yellow = "\u001B[33m";
  private final String red = "\u001B[31m";
  private final String green = "\u001B[32m";
  private final String resetColor = "\u001B[0m";
  private final String eol = "\r\n";

  private Orchestrator model;
  private UserInteraction view;

  @Before
  public void setup() {
    model = new ModelOrchestratorV2();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
  }

  @Test()
  public void testMenu() {
    Reader in = new StringReader("q q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    System.setOut(out);
    view = new UserInteraction(out,model);
    ih = new InteractionHandlerV2(
        in,
        model,
        view
    );
    ih.run();

    assertEquals("\u001B[32mChoose from the following:\u001B[0m\n"
        + "\u001B[33m0 - Set Commission Fees\u001B[0m\n"
        + "\u001B[33m1 - Load External Portfolio\u001B[0m\n"
        + "\u001B[33m2 - View Portfolio Composition\u001B[0m\n"
        + "\u001B[33m3 - Modify existing Portfolio\u001B[0m\n"
        + "\u001B[33m4 - Get Portfolio Value\u001B[0m\n"
        + "\u001B[33m5 - Create New Portfolio\u001B[0m\n"
        + "\u001B[33m6 - View Portfolio Performance\u001B[0m\n"
        + "\u001B[33m7 - Get Cost-Basis for Portfolio and Date\u001B[0m\n"
        + "\u001B[33mq/Q - Quit Application\u001B[0m",bytes.toString().strip());
  }

  @Test()
  public void testNegativeCommission() {
    Reader in = new StringReader("0 -1 q q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    System.setOut(out);
    view = new UserInteraction(out,model);
    ih = new InteractionHandlerV2(
        in,
        model,
        view
    );
    ih.run();

    assertEquals("\u001B[32mChoose from the following:\u001B[0m\n"
        + "\u001B[33m0 - Set Commission Fees\u001B[0m\n"
        + "\u001B[33m1 - Load External Portfolio\u001B[0m\n"
        + "\u001B[33m2 - View Portfolio Composition\u001B[0m\n"
        + "\u001B[33m3 - Modify existing Portfolio\u001B[0m\n"
        + "\u001B[33m4 - Get Portfolio Value\u001B[0m\n"
        + "\u001B[33m5 - Create New Portfolio\u001B[0m\n"
        + "\u001B[33m6 - View Portfolio Performance\u001B[0m\n"
        + "\u001B[33m7 - Get Cost-Basis for Portfolio and Date\u001B[0m\n"
        + "\u001B[33mq/Q - Quit Application\u001B[0m\n"
        + "\u001B[33mBy Default, we charge our users $1 per transaction (buy/sell).\u001B[0m\n"
        + "\u001B[33mThe new commission fees that you set, will be reset every time you quit the application.\u001B[0m\n"
        + "\u001B[33mPlease enter the Commission Fees per transaction that you would like to set for this time, or Q/q to quit\u001B[0m\n"
        + "\u001B[31mSorry, input did not match requirements!\u001B[0m\n"
        + "\u001B[32mChoose from the following:\u001B[0m\n"
        + "\u001B[33m0 - Set Commission Fees\u001B[0m\n"
        + "\u001B[33m1 - Load External Portfolio\u001B[0m\n"
        + "\u001B[33m2 - View Portfolio Composition\u001B[0m\n"
        + "\u001B[33m3 - Modify existing Portfolio\u001B[0m\n"
        + "\u001B[33m4 - Get Portfolio Value\u001B[0m\n"
        + "\u001B[33m5 - Create New Portfolio\u001B[0m\n"
        + "\u001B[33m6 - View Portfolio Performance\u001B[0m\n"
        + "\u001B[33m7 - Get Cost-Basis for Portfolio and Date\u001B[0m\n"
        + "\u001B[33mq/Q - Quit Application\u001B[0m",bytes.toString().strip());
  }

  @Test()
  public void testSellMoreThanRequired() {
    Reader in = new StringReader("2 040340 3 040340 AAPL,20,2022-11-17,SELL\nq 2 040340 q q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    System.setOut(out);
    view = new UserInteraction(out,model);
    ih = new InteractionHandlerV2(
        in,
        model,
        view
    );
    ih.run();

    assertEquals("\u001B[32mChoose from the following:\u001B[0m\n"
        + "\u001B[33m0 - Set Commission Fees\u001B[0m\n"
        + "\u001B[33m1 - Load External Portfolio\u001B[0m\n"
        + "\u001B[33m2 - View Portfolio Composition\u001B[0m\n"
        + "\u001B[33m3 - Modify existing Portfolio\u001B[0m\n"
        + "\u001B[33m4 - Get Portfolio Value\u001B[0m\n"
        + "\u001B[33m5 - Create New Portfolio\u001B[0m\n"
        + "\u001B[33m6 - View Portfolio Performance\u001B[0m\n"
        + "\u001B[33m7 - Get Cost-Basis for Portfolio and Date\u001B[0m\n"
        + "\u001B[33mq/Q - Quit Application\u001B[0m\n"
        + "040340.json\n"
        + "201162.json\n"
        + "407089.json\n"
        + "591884.json\n"
        + "760676.json\n"
        + "783853.json\n"
        + "907843.json\n"
        + "test.json\n"
        + "test2.json\n"
        + "test3.json\n"
        + "\u001B[33mEnter your portfolio ID:\u001B[0m\n"
        + "\u001B[33mOutput:\u001B[0m\n"
        + "+-------------+-------------+\n"
        + "| Stock Name  | Quantity    |\n"
        + "+-------------+-------------+\n"
        + "| AAPL        | 0.0         |\n"
        + "| TSLA        | 10.0        |\n"
        + "| GOOG        | 25.0        |\n"
        + "| IBM         | 15.0        |\n"
        + "+-------------+-------------+\n"
        + "\u001B[32mChoose from the following:\u001B[0m\n"
        + "\u001B[33m0 - Set Commission Fees\u001B[0m\n"
        + "\u001B[33m1 - Load External Portfolio\u001B[0m\n"
        + "\u001B[33m2 - View Portfolio Composition\u001B[0m\n"
        + "\u001B[33m3 - Modify existing Portfolio\u001B[0m\n"
        + "\u001B[33m4 - Get Portfolio Value\u001B[0m\n"
        + "\u001B[33m5 - Create New Portfolio\u001B[0m\n"
        + "\u001B[33m6 - View Portfolio Performance\u001B[0m\n"
        + "\u001B[33m7 - Get Cost-Basis for Portfolio and Date\u001B[0m\n"
        + "\u001B[33mq/Q - Quit Application\u001B[0m\n"
        + "040340.json\n"
        + "201162.json\n"
        + "407089.json\n"
        + "591884.json\n"
        + "760676.json\n"
        + "783853.json\n"
        + "907843.json\n"
        + "test.json\n"
        + "test2.json\n"
        + "test3.json\n"
        + "\u001B[33mEnter Portfolio ID you want to edit shares for:\u001B[0m\n"
        + "\u001B[33mEnter STOCK,QUANTITY,DATE,CALL, q/Q to stop entering\u001B[0m\n"
        + "\u001B[32mExample: AAPL,20,2020-10-13,BUY\u001B[0m\n"
        + "\u001B[33mOutput:\u001B[0m\n"
        + "\u001B[32mSaved the updated portfolio!\u001B[0m\n"
        + "\u001B[32mChoose from the following:\u001B[0m\n"
        + "\u001B[33m0 - Set Commission Fees\u001B[0m\n"
        + "\u001B[33m1 - Load External Portfolio\u001B[0m\n"
        + "\u001B[33m2 - View Portfolio Composition\u001B[0m\n"
        + "\u001B[33m3 - Modify existing Portfolio\u001B[0m\n"
        + "\u001B[33m4 - Get Portfolio Value\u001B[0m\n"
        + "\u001B[33m5 - Create New Portfolio\u001B[0m\n"
        + "\u001B[33m6 - View Portfolio Performance\u001B[0m\n"
        + "\u001B[33m7 - Get Cost-Basis for Portfolio and Date\u001B[0m\n"
        + "\u001B[33mq/Q - Quit Application\u001B[0m\n"
        + "040340.json\n"
        + "201162.json\n"
        + "407089.json\n"
        + "591884.json\n"
        + "760676.json\n"
        + "783853.json\n"
        + "907843.json\n"
        + "test.json\n"
        + "test2.json\n"
        + "test3.json\n"
        + "\u001B[33mEnter your portfolio ID:\u001B[0m\n"
        + "\u001B[33mOutput:\u001B[0m\n"
        + "+-------------+-------------+\n"
        + "| Stock Name  | Quantity    |\n"
        + "+-------------+-------------+\n"
        + "| AAPL        | 0.0         |\n"
        + "| TSLA        | 10.0        |\n"
        + "| GOOG        | 25.0        |\n"
        + "| IBM         | 15.0        |\n"
        + "+-------------+-------------+\n"
        + "\u001B[32mChoose from the following:\u001B[0m\n"
        + "\u001B[33m0 - Set Commission Fees\u001B[0m\n"
        + "\u001B[33m1 - Load External Portfolio\u001B[0m\n"
        + "\u001B[33m2 - View Portfolio Composition\u001B[0m\n"
        + "\u001B[33m3 - Modify existing Portfolio\u001B[0m\n"
        + "\u001B[33m4 - Get Portfolio Value\u001B[0m\n"
        + "\u001B[33m5 - Create New Portfolio\u001B[0m\n"
        + "\u001B[33m6 - View Portfolio Performance\u001B[0m\n"
        + "\u001B[33m7 - Get Cost-Basis for Portfolio and Date\u001B[0m\n"
        + "\u001B[33mq/Q - Quit Application\u001B[0m",bytes.toString().strip());
  }

}
