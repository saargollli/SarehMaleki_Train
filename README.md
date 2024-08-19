I chose the Apache Commons SCXML library because it makes working with state machines much easier.
The library is widely available, regularly updated, and supports a standard format for defining state machines, 
so you don't have to create everything from scratch. It handles state changes and events efficiently and runs smoothly,
even in complex situations. Now, I will explain the code of this part.

org.apache.commons.scxml2.SCXMLExecutor: This class manages the overall state of the machine,
including the execution of the state machine, handling state transitions, and processing events.

org.apache.commons.scxml2.env.SimpleDispatcher: It handles the delivery of 
events to the appropriate state or transition.

org.apache.commons.scxml2.env.jexl.JexlEvaluator: It interprets and processes the 
conditions and actions defined in the state machine.

org.apache.commons.scxml2.io.SCXMLReader: It converts the SCXML document into a 
format that the SCXMLExecutor can execute.

org.apache.commons.scxml2.model.SCXML: It contains the entire structure of the 
state machine, including states, transitions, and events.

org.apache.commons.scxml2.env.SimpleErrorReporter: It logs errors that occur when the state machine is running.

org.apache.commons.scxml2.TriggerEvent: It carries information about the event, 
which the state machine uses to determine the next state.

org.apache.commons.scxml2.model.EnterableState, model.TransitionalState, model.OnEntry: It includes information 
about the state's entry and the logic for moving from one state to another based on events or conditions (using Onentery in state machines).

org.apache.commons.scxml2.model.Raise, model. Action: these are about the rise in state machines.
Actions might include raising events, running scripts, or performing other tasks when a state is entered or exited.

The below line creates a Logger instance for the TrainStateMachineWithInput class, used to log messages like info, warnings, and errors
private static final Logger logger = Logger.getLogger(TrainStateMachineWithInput.class.getName());

executor manages and runs the SCXML state machine.


Main function: This function reads the path of the SCXML file from input, sets the logger to capture 
high-level information, such as warnings, and creates a new instance of TrainStateMachineWithInput. 
It then calls the Run function with the SCXML_FILE_PATH argument, which is the path to the SCXML file.

public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("give your path please: (example: /home/sareh/Documents/task2.scxml)");
    String SCXML_FILE_PATH =scanner.nextLine();
    Logger.getLogger("org.apache.commons.scxml2").setLevel(Level.WARNING);
    TrainStateMachineWithInput trainStateMachine = new TrainStateMachineWithInput();
    trainStateMachine.Run(SCXML_FILE_PATH);
}

Run function: This function initializes the state machine with the given SCXML_FILE_PATH and reads 
user input in a loop using a Scanner. It processes events to trigger state transitions until the 
state machine reaches a final state or the user types "exit" to stop. If the user inputs an invalid 
event, it logs a warning; otherwise, it triggers the event and displays the current state details.

public void Run(String SCXML_FILE_PATH) {
    try (Scanner scanner = new Scanner(System.in)) {
        initializeStateMachine(SCXML_FILE_PATH);
        while (!executor.getStatus().isFinal()) {
            String event = promptForEvent(scanner);
            if (event.equalsIgnoreCase("exit")) {
                logger.info("Exiting the state machine...");
                break;
            }
            if (isValidEvent(event)) {
                triggerEventAndPrintDetailedState(event);
            } else {
                logger.warning("Invalid event. Please enter 'Seen', 'NotSeen', 'leaving', or 'exit'.");
            }
        }
    } catch (Exception e) {
        logger.log(Level.SEVERE, "An error occurred while running the state machine.", e);
    } finally {
        shutdownStateMachine();
    }
}

The below line of Run function calls the initializeStateMachine function:
initializeStateMachine(SCXML_FILE_PATH);

Until the state machine reaches a final state, the promptForEvent function is called to read events from input:
String event = promptForEvent(scanner);

The below code calls the isValidEvent function, which checks the validation of the input:
if (isValidEvent(event)) {
    triggerEventAndPrintDetailedState(event);
}

The  isValidEvent function works as below:
private boolean isValidEvent(String event) {
    return event.equals("Seen") || event.equals("NotSeen") || event.equals("leaving") || event.equals("approaching");
}

If the input is valid, the triggerEventAndPrintDetailedState method will be invoked. This method logs the event being triggered, processes the event within the state machine using the executor:
private void triggerEventAndPrintDetailedState(String event) throws Exception {
    logger.info("Triggering event: " + event);
    executor.triggerEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT));
    System.out.println();
}

The code below only functions when the state machine reaches a final state. I have written it to enhance my code so that it can handle all types of state machines, not just those without a final state.
finally {
    //If there was a final state
    shutdownStateMachine();
}

initializeStateMachine function: This function sets up the state machine by reading the SCXML file specified by 
SCXML_FILE_PATH and creating a new SCXMLExecutor instance with necessary components. It parses the SCXML into an SCXML object, 
assigns it to the executor, and starts execution (.executor.go();). If any errors occur during this process, they are logged as SEVER.

private void initializeStateMachine(String SCXML_FILE_PATH) throws Exception {
    try (InputStream scxmlInputStream = new FileInputStream(new File(SCXML_FILE_PATH))) {
        SCXML scxml = SCXMLReader.read(scxmlInputStream);
        executor = new SCXMLExecutor(new JexlEvaluator(), new SimpleDispatcher(), new SimpleErrorReporter());
        executor.setStateMachine(scxml);
        executor.go();
        logger.info("State machine initialized successfully.");
    } catch (Exception e) {
        logger.log(Level.SEVERE, "Failed to initialize state machine.", e);
        throw e;
    }
}

promptForEvent function: This function read the input provided by user from, there are three types of inputs:
-Seen: for event seen
-NotSeen: when the seen event don’t occur
-leaving: I added an extra feature that allows the user to send a specific leaving event, different 
from the entry event, to the leaving state in the controller state machine.
-approaching: I added an extra feature that allows the user to send a specific approaching event, 
different from the entry event, to the approach state in the controller state machine.
private String promptForEvent(Scanner scanner) {
    System.out.println("Enter an event (Seen, NotSeen, leaving, approaching) or 'exit' to quit:");
    return scanner.nextLine();
}

_Applicability: This code is suitable for all state machines; you only need to update the valid events in the isValidEvent function to fit your specific requirements._
