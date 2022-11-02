package controller;

import model.ModelOrchestrator;
import org.junit.Before;
import org.junit.Test;
import view.UserInteraction;

import java.io.*;
import java.util.NoSuchElementException;

//import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Tests the controller.
 */
public class InteractionHandlerTest {

  InteractionHandler ih;

  private final String yellow = "\u001B[33m";
  private final String red = "\u001B[31m";
  private final String green = "\u001B[32m";
  private final String resetColor = "\u001B[0m";
  private final String eol = "\r\n";

  @Before
  public void setup() {
  }

  @Test(expected = NoSuchElementException.class)
  public void testHomePage() {
    Reader in = new StringReader(" ");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();

    String expected = "";
    assertEquals(expected, bytes.toString());

    expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        yellow + "Please provide the path to load a CSV" + resetColor;
    assertEquals(expected, new String(bytes.toByteArray()).strip());
  }

  @Test
  public void incorrectCSVFileLoad() {
    Reader in = new StringReader("1 exe/123.csv q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();

    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        yellow + "Please provide the path to load a CSV" + resetColor + eol +
        red + "File not found, please enter a correct path" + resetColor + eol +
        green + "File read successful. Portfolio ID: " + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor;

    assertEquals(expected, new String(bytes.toByteArray()).strip());
  }

  @Test
  public void csvReadSuccessTest() {
    String path = "C:\\Users\\DELL\\Downloads\\customPortfolio.csv";
    Reader in = new StringReader("1 " + path + " q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();

    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        yellow + "Please provide the path to load a CSV" + resetColor + eol +
        green + "File read successful: 925927.csv" + resetColor + eol +
        green + "File read successful. Portfolio ID: 925972" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor;
    assertEquals(expected, new String(bytes.toByteArray()).strip());
  }

  @Test
  public void listOfExistingPortfolios() {

    Reader in = new StringReader("2 q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();

    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        green + "Welcome! Please enter your portfolio number from the options: " + resetColor + eol
        +
        yellow + "Pick from existing portfolios:" + resetColor + eol +
        "057349.csv" + eol
        + "176086.csv" + eol
        + "195504.csv" + eol
        + "391410.csv" + eol
        + "440689.csv" + eol
        + "495934.csv" + eol
        + "579270.csv" + eol
        + "610189.csv" + eol
        + "643686.csv" + eol
        + "824679.csv" + eol
        + "845310.csv" + eol
        + "925972.csv" + eol
        + "949732.csv" + eol
        + "978216.csv" + eol
        + "myPort.csv" + eol;
    assertEquals(expected, new String(bytes.toByteArray()).strip());

  }

  @Test
  public void invalidPortfolioNameInput() {
    Reader in = new StringReader("2 test q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();

    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        green + "Welcome! Please enter your portfolio number from the options: " + resetColor + eol
        +
        yellow + "Pick from existing portfolios:" + resetColor + eol +
        "057349.csv" + eol
        + "176086.csv" + eol
        + "195504.csv" + eol
        + "391410.csv" + eol
        + "440689.csv" + eol
        + "495934.csv" + eol
        + "579270.csv" + eol
        + "610189.csv" + eol
        + "643686.csv" + eol
        + "824679.csv" + eol
        + "845310.csv" + eol
        + "925972.csv" + eol
        + "949732.csv" + eol
        + "978216.csv" + eol
        + "myPort.csv" + eol +
        red + "Sorry, input did not match requirements!" + resetColor;

    assertEquals(expected, new String(bytes.toByteArray()).strip());
  }

  @Test
  public void displayPortfolioTest() {
    Reader in = new StringReader("2 057349 q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();
    String result = "+-------------+-------------+" + eol
        + "| Stock Name  | Quantity    |" + eol
        + "+-------------+-------------+" + eol
        + "| MSFT        | 10          | " + eol
        + "| TSLA        | 20          | " + eol
        + "+-------------+-------------+" + eol;
    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        green + "Welcome! Please enter your portfolio number from the options: " + resetColor + eol
        +
        yellow + "Pick from existing portfolios:" + resetColor + eol +
        "057349.csv" + eol
        + "176086.csv" + eol
        + "195504.csv" + eol
        + "391410.csv" + eol
        + "440689.csv" + eol
        + "495934.csv" + eol
        + "579270.csv" + eol
        + "610189.csv" + eol
        + "643686.csv" + eol
        + "824679.csv" + eol
        + "845310.csv" + eol
        + "925972.csv" + eol
        + "949732.csv" + eol
        + "978216.csv" + eol
        + "myPort.csv" + eol +
        green + "Your portfolio number:057349" + resetColor + eol +
        yellow + "Here's your data!" + resetColor + eol +
        result +
        yellow + "Check portfolio value on a given date (YYYY-MM-DD) [avoid weekends] "
        + "or exit:'b/B':" + resetColor;

    assertEquals(expected.strip(), new String(bytes.toByteArray()).strip());
  }

  @Test
  public void displayPortfolioValue() {
    Reader in = new StringReader("2 057349 2022-11-02 q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();

    String result = "+-------------+-------------+" + eol
        + "| Stock Name  | Quantity    |" + eol
        + "+-------------+-------------+" + eol
        + "| MSFT        | 10          | " + eol
        + "| TSLA        | 20          | " + eol
        + "+-------------+-------------+" + eol;
    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        green + "Welcome! Please enter your portfolio number from the options: " + resetColor + eol
        +
        yellow + "Pick from existing portfolios:" + resetColor + eol +
        "057349.csv" + eol
        + "176086.csv" + eol
        + "195504.csv" + eol
        + "391410.csv" + eol
        + "440689.csv" + eol
        + "495934.csv" + eol
        + "579270.csv" + eol
        + "610189.csv" + eol
        + "643686.csv" + eol
        + "824679.csv" + eol
        + "845310.csv" + eol
        + "925972.csv" + eol
        + "949732.csv" + eol
        + "978216.csv" + eol
        + "myPort.csv" + eol +
        green + "Your portfolio number:057349" + resetColor + eol +
        yellow + "Here's your data!" + resetColor + eol +
        result +
        yellow + "Check portfolio value on a given date (YYYY-MM-DD) [avoid weekends] "
        + "or exit:'b/B':" + resetColor + eol +
        "+-------------+-------------+-------------+" + eol
        + "| Stock Name  | Quantity    | Value       |" + eol
        + "+-------------+-------------+-------------+" + eol
        + "| MSFT        | 10          | 2201.00     |" + eol
        + "| TSLA        | 20          | 4299.60     |" + eol
        + "| Total       | -           | 6500.60     |" + eol
        + "+-------------+-------------+-------------+" + eol +
        yellow + "Check portfolio value on a given date (YYYY-MM-DD) [avoid weekends] "
        + "or exit:'b/B':" + resetColor;

    assertEquals(expected.strip(), new String(bytes.toByteArray()).strip());
  }

  @Test
  public void weekendDateCheck() {
    Reader in = new StringReader("2 057349 2022-10-30 q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();
    String result = "+-------------+-------------+" + eol
        + "| Stock Name  | Quantity    |" + eol
        + "+-------------+-------------+" + eol
        + "| MSFT        | 10          | " + eol
        + "| TSLA        | 20          | " + eol
        + "+-------------+-------------+" + eol;
    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        green + "Welcome! Please enter your portfolio number from the options: " + resetColor + eol
        +
        yellow + "Pick from existing portfolios:" + resetColor + eol +
        "057349.csv" + eol
        + "147157.csv" + eol
        + "176086.csv" + eol
        + "195504.csv" + eol
        + "325359.csv" + eol
        + "391410.csv" + eol
        + "416383.csv" + eol
        + "440689.csv" + eol
        + "440750.csv" + eol
        + "495934.csv" + eol
        + "579270.csv" + eol
        + "610189.csv" + eol
        + "643686.csv" + eol
        + "662122.csv" + eol
        + "824679.csv" + eol
        + "845310.csv" + eol
        + "925972.csv" + eol
        + "949732.csv" + eol
        + "957759.csv" + eol
        + "978216.csv" + eol
        + "myPort.csv" + eol +
        green + "Your portfolio number:057349" + resetColor + eol +
        yellow + "Here's your data!" + resetColor + eol +
        result +
        yellow + "Check portfolio value on a given date (YYYY-MM-DD) [avoid weekends] "
        + "or exit:'b/B':" + resetColor + eol +
        yellow + "Sorry, the date entered is a weekend, please re-enter:" + resetColor + eol +
        yellow + "Check portfolio value on a given date (YYYY-MM-DD) [avoid weekends] "
        + "or exit:'b/B':" + resetColor;

    assertEquals(expected.strip(), new String(bytes.toByteArray()).strip());
  }

  @Test
  public void createPortfolioTest() {
    Reader in = new StringReader("3 q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();

    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        green + "Welcome to our platform!" + resetColor + eol +
        yellow + "Enter 'F/f' to finish entering stock details" + resetColor + eol +
        yellow + "Please enter the details like so: Stock,Quantity:" + resetColor + eol +
        yellow + "Example: AAPL,20" + resetColor;

    assertEquals(expected.strip(), new String(bytes.toByteArray()).strip());
  }

  @Test
  public void multipleStocksEnteringTest() {
    Reader in = new StringReader("3 AAPL,20 IBM,30 q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();

    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        green + "Welcome to our platform!" + resetColor + eol +
        yellow + "Enter 'F/f' to finish entering stock details" + resetColor + eol +
        yellow + "Please enter the details like so: Stock,Quantity:" + resetColor + eol +
        yellow + "Example: AAPL,20" + resetColor + eol + eol + eol +
        yellow + "Enter 'F/f' to finish entering stock details" + resetColor + eol +
        yellow + "Please enter the details like so: Stock,Quantity:" + resetColor + eol +
        yellow + "Example: AAPL,20" + resetColor + eol + eol +
        "AAPL,20" + eol + eol +
        yellow + "Enter 'F/f' to finish entering stock details" + resetColor + eol +
        yellow + "Please enter the details like so: Stock,Quantity:" + resetColor + eol +
        yellow + "Example: AAPL,20" + resetColor + eol + eol +
        "AAPL,20" + eol + "IBM,30";

    assertEquals(expected, new String(bytes.toByteArray()).strip());
  }

  @Test
  public void manualPortfolioCreatedTest() {
    Reader in = new StringReader("3 AAPL,20 IBM,30 f q");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    ih = new InteractionHandler(
        in,
        out
    );
    ih.run();

    String expected = "" +
        green + "Welcome To Aaka-Sam Stock Trading!" + resetColor + eol +
        yellow + "You can always quit the platform by pressing 'q'" + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor + eol +
        green + "Welcome to our platform!" + resetColor + eol +
        yellow + "Enter 'F/f' to finish entering stock details" + resetColor + eol +
        yellow + "Please enter the details like so: Stock,Quantity:" + resetColor + eol +
        yellow + "Example: AAPL,20" + resetColor + eol + eol + eol +
        yellow + "Enter 'F/f' to finish entering stock details" + resetColor + eol +
        yellow + "Please enter the details like so: Stock,Quantity:" + resetColor + eol +
        yellow + "Example: AAPL,20" + resetColor + eol + eol +
        "AAPL,20" + eol + eol +
        yellow + "Enter 'F/f' to finish entering stock details" + resetColor + eol +
        yellow + "Please enter the details like so: Stock,Quantity:" + resetColor + eol +
        yellow + "Example: AAPL,20" + resetColor + eol + eol +
        "AAPL,20" + eol + "IBM,30" + eol + eol +
        "Portfolio ID: 147157 Saved!" + eol +
        green + "Thank you for using Aaka-Sam Trading." + resetColor + eol +
        green + "Stay tuned for more features. :)" + eol + resetColor + eol +
        yellow + "Select from '1/2/3':" + resetColor + eol +
        yellow + "1. Load External Portfolio" + resetColor + eol +
        yellow + "2. Access existing Portfolio" + resetColor + eol +
        yellow + "3. Create new Portfolio" + resetColor;

    assertEquals(expected, new String(bytes.toByteArray()).strip());
  }


}
