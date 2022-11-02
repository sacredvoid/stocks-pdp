package controller;

import model.ModelOrchestrator;
import org.junit.Before;
import org.junit.Test;
import view.UserInteraction;

import java.io.*;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class InteractionHandlerTest {

    UserInteraction ui;
    InteractionHandler ih;
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

        String expected =
                "Welcome To Aaka-Sam Stock Trading!\n" +
                "You can always quit the platform by pressing 'q'\n" +
                "Select from '1/2/3':\n" +
                "1. Load External Portfolio\n" +
                "2. Access existing Portfolio\n" +
                "3. Create new Portfolio\n" +
                "Please provide the path to load a CSV";
        assertEquals(expected, new String(bytes.toByteArray()));
    }
}
