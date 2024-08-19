# Software Project

## Overview

`TrainStateMachineWithInput` is a Java-based command-line application that executes a state machine defined in an SCXML (State Chart XML) file. Users can trigger state transitions by inputting events via the command line. This tool leverages the Apache Commons SCXML library to manage state transitions, providing a flexible way to model and execute workflows.

## Key Features

- **SCXML Execution**: Load and run a state machine defined in an SCXML file.
- **User Interaction**: Users can interact with the state machine by inputting events to trigger transitions.
- **Custom Events**: Supports additional events such as `leaving` and `approaching` alongside predefined ones.
- **Logging**: Logs the execution process and state transitions for easier debugging.

## Prerequisites

Before running the project, ensure you have the following installed:

- Java Development Kit (JDK) 8 or later (Java 21 is recommended)
- Gradle (or use the provided Gradle wrapper)
- Internet connection for downloading dependencies

## Setup and Installation

### 1. Clone the Repository

Clone the project from the following GitHub repository:

```bash
git clone https://github.com/saargollli/SarehMaleki_Train.git
cd SarehMaleki_Train
```

### 2. Build the Project

If Gradle is installed on your system, you can build the project using:

```bash
gradle build
```

Alternatively, you can use the provided Gradle wrapper:

```bash
./gradlew build
```

### 3. Running the Application

You can run the application directly through Gradle or by executing the generated JAR file.

#### Option 1: Run with Gradle

```bash
gradle run
```

#### Option 2: Run the Fat JAR

After building the project, a fat JAR (with all dependencies bundled) will be created in the `build/libs/` directory. You can execute it using:

```bash
java -jar build/libs/TrainStateMachineWithInput2.jar
```

### 4. Provide the SCXML File Path

Upon running the application, you will be prompted to provide the path to your SCXML file. For example:

```plaintext
give your path please: (example: /home/user/task2.scxml)
```

### 5. Enter Events

After providing the SCXML file, the application will prompt you to enter events. You can input one of the supported events:

- `Seen`
- `NotSeen`
- `leaving`
- `approaching`
- `exit` (to terminate the program)

### Example Execution

```plaintext
give your path please: (example: /home/user/task2.scxml)
/home/user/train_state_machine.scxml
Enter an event (Seen, NotSeen, leaving, approaching) or 'exit' to quit:
Seen
```

## Configuration and Customization

### Build Script (Gradle)

This project uses Gradle as the build tool, and the `build.gradle` file is pre-configured with the necessary plugins and dependencies.

#### Key Plugins

- **Java Plugin**: Supports compiling Java code and managing the project lifecycle.
- **Application Plugin**: Allows running the application from the command line.
- **Shadow Plugin**: Bundles all dependencies into a single JAR for easier distribution.

#### Dependencies

The following key dependencies are included in the `build.gradle` file:

- `de.dfki.cos.basys.common:scxml`: For SCXML state machine execution.
- `commons-logging`: For logging.
- `org.apache.commons:commons-jexl3`: For expression evaluation.
- `commons-beanutils`: For JavaBeans manipulation.
- `JUnit 5`: For testing.

#### Toolchain

The project is set to use Java 21. If you're using a different version, update the toolchain settings in `build.gradle`.

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
```

### Logging

The application uses Java's built-in logging framework. By default, it only shows warning messages or more severe logs from the SCXML library. You can adjust the logging level by modifying the following line in the `main` method:

```java
Logger.getLogger("org.apache.commons.scxml2").setLevel(Level.WARNING);
```

### Shadow JAR Configuration

The Shadow plugin is configured to package all dependencies into a fat JAR. The output file is named `TrainStateMachineWithInput2.jar`, and it is located in the `build/libs/` directory.

```groovy
tasks.withType(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar) {
    archiveBaseName.set("TrainStateMachineWithInput2")
    archiveClassifier.set("")
    archiveVersion.set("")
    mergeServiceFiles()
    zip64 = true
}
