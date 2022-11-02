package controller;

import model.ModelOrchestrator;
import org.junit.Before;
import org.junit.Test;
import view.UserInteraction;

import java.io.*;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class InteractionHandlerTest {

    ModelOrchestrator morch;
    UserInteraction ui;
    InteractionHandler ih;
    @Before
    public void setup() {
        morch = new ModelOrchestrator();
        ui = new UserInteraction();
    }

    @Test(expected = NoSuchElementException.class)
    public void testHomePage() {
        Reader in = new StringReader(" ");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes);
        ih = new InteractionHandler(
                morch,
                ui,
                in,
                out
        );
        ih.run();

        String expected = "";
        assertEquals(expected, bytes.toString());
    }

    @Test
    public void testLoadPortfolio() {
        Reader in = new StringReader("1");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes);
        ih = new InteractionHandler(
                morch,
                ui,
                in,
                out
        );
        ih.run();

        String expected = "";
        assertEquals(expected, bytes.toString());
    }
}
