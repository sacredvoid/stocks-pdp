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

public class InteractionHandlerTest {

    UserInteraction ui;
    InteractionHandler ih;

    private final String yellow = "\u001B[33m";
    private final String red = "\u001B[31m";
    private final String green = "\u001B[32m";
    private final String resetColor = "\u001B[0m";
    private final String eol = "\r\n";

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
    }

    @Test
    public void testLoadPortfolio() {
        Reader in = new StringReader("1 q");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes);
        ih = new InteractionHandler(
                in,
                out
        );
        ih.run();

        String expected =""+
                green+"Welcome To Aaka-Sam Stock Trading!"+resetColor+eol+
                yellow+"You can always quit the platform by pressing 'q'"+resetColor+eol+
                yellow+"Select from '1/2/3':"+resetColor+eol+
                yellow+"1. Load External Portfolio"+resetColor+eol+
                yellow+"2. Access existing Portfolio"+resetColor+eol+
                yellow+"3. Create new Portfolio"+resetColor+eol+
                yellow+"Please provide the path to load a CSV"+resetColor;
        assertEquals(expected, new String(bytes.toByteArray()).strip());
    }

    @Test
    public void incorrectCSVFileLoad(){
        Reader in = new StringReader("1 exe/123.csv q");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes);
        ih = new InteractionHandler(
            in,
            out
        );
        ih.run();

        String expected =""+
            green+"Welcome To Aaka-Sam Stock Trading!"+resetColor+eol+
            yellow+"You can always quit the platform by pressing 'q'"+resetColor+eol+
            yellow+"Select from '1/2/3':"+resetColor+eol+
            yellow+"1. Load External Portfolio"+resetColor+eol+
            yellow+"2. Access existing Portfolio"+resetColor+eol+
            yellow+"3. Create new Portfolio"+resetColor+eol+
            yellow+"Please provide the path to load a CSV"+resetColor+eol+
            red+"File not found, please enter a correct path"+resetColor+eol+
            green+"File read successful. Portfolio ID: "+resetColor+eol+
            yellow+"Select from '1/2/3':"+resetColor+eol+
            yellow+"1. Load External Portfolio"+resetColor+eol+
            yellow+"2. Access existing Portfolio"+resetColor+eol+
            yellow+"3. Create new Portfolio"+resetColor;

        assertEquals(expected,new String(bytes.toByteArray()).strip());
    }
    
//    @Test
//
//    public void csvReadSuccessTest(){
//        String path = "C:\\Users\\DELL\\Downloads\\customPortfolio.csv";
//        Reader in = new StringReader("1 "+path+" q");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        PrintStream out = new PrintStream(bytes);
//        ih = new InteractionHandler(
//            in,
//            out
//        );
//        ih.run();
//
//        String expected =""+
//            green+"Welcome To Aaka-Sam Stock Trading!"+resetColor+eol+
//            yellow+"You can always quit the platform by pressing 'q'"+resetColor+eol+
//            yellow+"Select from '1/2/3':"+resetColor+eol+
//            yellow+"1. Load External Portfolio"+resetColor+eol+
//            yellow+"2. Access existing Portfolio"+resetColor+eol+
//            yellow+"3. Create new Portfolio"+resetColor+eol+
//            yellow+"Please provide the path to load a CSV"+resetColor+eol+
//            green+"File read successful: 925927.csv"+resetColor+eol+
//            green+"File read successful. Portfolio ID: 925972"+resetColor+eol+
//            yellow+"Select from '1/2/3':"+resetColor+eol+
//            yellow+"1. Load External Portfolio"+resetColor+eol+
//            yellow+"2. Access existing Portfolio"+resetColor+eol+
//            yellow+"3. Create new Portfolio"+resetColor;
//        assertEquals(expected,new String(bytes.toByteArray()).strip());
//    }

    @Test
    public void listOfExistingPortfolios(){

        Reader in = new StringReader("2 q");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes);
        ih = new InteractionHandler(
            in,
            out
        );
        ih.run();

        String expected =""+
            green+"Welcome To Aaka-Sam Stock Trading!"+resetColor+eol+
            yellow+"You can always quit the platform by pressing 'q'"+resetColor+eol+
            yellow+"Select from '1/2/3':"+resetColor+eol+
            yellow+"1. Load External Portfolio"+resetColor+eol+
            yellow+"2. Access existing Portfolio"+resetColor+eol+
            yellow+"3. Create new Portfolio"+resetColor+eol+
            green+"Welcome! Please enter your portfolio number from the options: "+resetColor+eol+
            yellow+"Pick from existing portfolios:"+resetColor+eol+
            "057349.csv"+eol
            + "176086.csv"+eol
            + "195504.csv"+eol
            + "391410.csv"+eol
            + "440689.csv"+eol
            + "495934.csv"+eol
            + "579270.csv"+eol
            + "610189.csv"+eol
            + "643686.csv"+eol
            + "925972.csv"+eol
            + "978216.csv"+eol
            + "myPort.csv";
        assertEquals(expected,new String(bytes.toByteArray()).strip());

    }

    @Test
    public void invalidPortfolioNameInput(){
        Reader in = new StringReader("2 test q");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes);
        ih = new InteractionHandler(
            in,
            out
        );
        ih.run();

        String expected =""+
            green+"Welcome To Aaka-Sam Stock Trading!"+resetColor+eol+
            yellow+"You can always quit the platform by pressing 'q'"+resetColor+eol+
            yellow+"Select from '1/2/3':"+resetColor+eol+
            yellow+"1. Load External Portfolio"+resetColor+eol+
            yellow+"2. Access existing Portfolio"+resetColor+eol+
            yellow+"3. Create new Portfolio"+resetColor+eol+
            green+"Welcome! Please enter your portfolio number from the options: "+resetColor+eol+
            yellow+"Pick from existing portfolios:"+resetColor+eol+
            "057349.csv"+eol
            + "176086.csv"+eol
            + "195504.csv"+eol
            + "391410.csv"+eol
            + "440689.csv"+eol
            + "495934.csv"+eol
            + "579270.csv"+eol
            + "610189.csv"+eol
            + "643686.csv"+eol
            + "925972.csv"+eol
            + "978216.csv"+eol
            + "myPort.csv"+eol+
            red+"Sorry, input did not match requirements!"+resetColor;

        assertEquals(expected,new String(bytes.toByteArray()).strip());
    }

    @Test
    public void displayPortfolioTest(){
        Reader in = new StringReader("2 057349 q");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes);
        ih = new InteractionHandler(
            in,
            out
        );
        ih.run();
        String result = "+-------------+-------------+"+"\n"
            + "| Stock Name  | Quantity    |"+eol
            + "+-------------+-------------+"+eol
            + "| MSFT        | 10          | "+eol
            + "| TSLA        | 20          | "+eol
            + "+-------------+-------------+"+eol;
        String expected =""+
            green+"Welcome To Aaka-Sam Stock Trading!"+resetColor+eol+
            yellow+"You can always quit the platform by pressing 'q'"+resetColor+eol+
            yellow+"Select from '1/2/3':"+resetColor+eol+
            yellow+"1. Load External Portfolio"+resetColor+eol+
            yellow+"2. Access existing Portfolio"+resetColor+eol+
            yellow+"3. Create new Portfolio"+resetColor+eol+
            green+"Welcome! Please enter your portfolio number from the options: "+resetColor+eol+
            yellow+"Pick from existing portfolios:"+resetColor+eol+
            "057349.csv"+eol
            + "176086.csv"+eol
            + "195504.csv"+eol
            + "391410.csv"+eol
            + "440689.csv"+eol
            + "495934.csv"+eol
            + "579270.csv"+eol
            + "610189.csv"+eol
            + "643686.csv"+eol
            + "925972.csv"+eol
            + "978216.csv"+eol
            + "myPort.csv"+eol+
            green+"Your portfolio number:057349"+resetColor+eol+
            yellow+"Here's your data!"+resetColor+eol+
            result+
            yellow+"Check portfolio value on a given date (YYYY-MM-DD) [avoid weekends] "
            + "or exit:'b/B':"+resetColor;

        assertEquals(expected,new String(bytes.toByteArray()).strip());

    }

}
