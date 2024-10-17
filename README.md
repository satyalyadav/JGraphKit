# CSE464 Course Project

## Building the Project

To build the project, run the following command in the project root directory:

```
mvn package
```

This command will compile the code, run the tests, and create a JAR file in the `target` directory.

## Running the Project

After building the project, you can run the `Main` class using the following command:

```
java -cp target/CSE464-2023-syadav42-1.0-SNAPSHOT.jar Main
```

## Features and Usage

### 1. Parse a DOT graph file

```java
GraphManager manager = new GraphManager();
manager.parseGraph("path/to/your/input.dot");
System.out.println(manager.toString());
```

### 2. Add nodes

```java
manager.addNode("NewNode");
manager.addNodes(new String[]{"Node1", "Node2", "Node3"});
```

### 3. Add edges

```java
manager.addEdge("Node1", "Node2");
```

### 4. Output to DOT file and PNG

```java
manager.outputDOTGraph("path/to/output.dot");
manager.outputGraphics("path/to/output.png", "png");
```

## Running Tests

To run the tests, use the following command:

```
mvn test
```

## GitHub Commits

- Feature 1 (Parse Graph): [Link to commit]
- Feature 2 (Add Nodes): [Link to commit]
- Feature 3 (Add Edges): [Link to commit]
- Feature 4 (Output Graph): [Link to commit]

## Screenshots

[Include screenshots of the expected output for each feature here]