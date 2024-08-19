package org.example;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.env.SimpleDispatcher;
import org.apache.commons.scxml2.env.jexl.JexlEvaluator;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.SCXML;
import org.apache.commons.scxml2.env.SimpleErrorReporter;
import org.apache.commons.scxml2.TriggerEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.scxml2.TriggerEvent.*;

public class TrainStateMachineWithInput {

    private static final Logger logger = Logger.getLogger(TrainStateMachineWithInput.class.getName());
    private SCXMLExecutor executor;

    public static void main(String[] args) {
        //reading path to the SCXML file provided by the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("give your path please: (example: /home/sareh/Documents/task2.scxml)");
        String SCXML_FILE_PATH =scanner.nextLine();

        //only show warning messages or more severe logs
        Logger.getLogger("org.apache.commons.scxml2").setLevel(Level.WARNING);

        //creating new instance of TrainStateMachineWithInput, and call Run function with path argument
        TrainStateMachineWithInput trainStateMachine = new TrainStateMachineWithInput();
        trainStateMachine.Run(SCXML_FILE_PATH);
    }

    public void Run(String SCXML_FILE_PATH) {
        try (Scanner scanner = new Scanner(System.in)) {
            initializeStateMachine(SCXML_FILE_PATH);
            // Start a while loop that continues until the state machine reaches its final state
            // The loop uses executor.getStatus().isFinal() to check if the state machine has completed
            while (!executor.getStatus().isFinal()) {
                // Prompt the user to input an event and store it in the 'event' variable
                String event = promptForEvent(scanner);

                // If the user enters "exit" as the event, terminate the state machine
                if (event.equalsIgnoreCase("exit")) {
                    logger.info("Exiting the state machine...");
                    break;
                }
                // If the user enters "leaving", "Seen", "NotSeen", or "approachimg" as the event
                if (isValidEvent(event)) {
                    triggerEventAndPrintDetailedState(event);
                } else {
                    // If the user don't enter "exit", "leaving", "Seen", "NotSeen", or "approachimg" as the event
                    logger.warning("Invalid event. Please enter 'Seen', 'NotSeen', 'leaving', or 'exit'.");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while running the state machine.", e);
        } finally {
            //If there was a final state
            shutdownStateMachine();
        }
    }

    // initializing the state machine
    private void initializeStateMachine(String SCXML_FILE_PATH) throws Exception {
        // Load SCXML file, initialize the state machine executor, and start execution
        try (InputStream scxmlInputStream = new FileInputStream(new File(SCXML_FILE_PATH))) {
            SCXML scxml = SCXMLReader.read(scxmlInputStream);
            executor = new SCXMLExecutor(new JexlEvaluator(), new SimpleDispatcher(), new SimpleErrorReporter());
            executor.setStateMachine(scxml);
            executor.go();
            logger.info("State machine initialized successfully.");
        } catch (Exception e) {
            // Log initialization failure
            logger.log(Level.SEVERE, "Failed to initialize state machine.", e);
            throw e;
        }
    }

    //reading event from the input, events can be Seen, NotSeen, leaving, and approaching.
    // the leaving and approacching is my additional feature.
    private String promptForEvent(Scanner scanner) {
        System.out.println("Enter an event (Seen, NotSeen, leaving, approaching) or 'exit' to quit:");
        return scanner.nextLine();
    }

    //checking the validation of the input
    private boolean isValidEvent(String event) {
        return event.equals("Seen") || event.equals("NotSeen") || event.equals("leaving") || event.equals("approaching");
    }

    private void triggerEventAndPrintDetailedState(String event) throws Exception {
        // Log the event being triggered
        logger.info("Triggering event: " + event);

        // Trigger the event in the state machine and process it
        executor.triggerEvent(new TriggerEvent(event, SIGNAL_EVENT));
        // New usage (check documentation for exact method)
//        TriggerEvent newEvent = TriggerEvent.createSignalEvent(event); // Hypothetical method
//        executor.triggerEvent(newEvent);

        System.out.println();
    }
    // for final states
    private void shutdownStateMachine() {
        if (executor != null) {
            logger.info("Shutting down the state machine...");
        }
    }
}