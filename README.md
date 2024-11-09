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

### Example input
```
input.dot:

digraph G {
  A -> B;
  B -> C;
  C -> A;
}
```

### 1. Parse a DOT graph file

```java
GraphManager manager = new GraphManager();
manager.parseGraph("path/to/your/input.dot");
System.out.println(manager.toString());
```

#### Output:
```
Parsing input.dot file...
Parsing file: input.dot
Parsed graph: digraph "G" {
"A" -> "B"
"B" -> "C"
"C" -> "A"
}
Successfully parsed input.dot

Initial Graph Information:
Number of nodes: 3
Node labels: [A, C, B]
Number of edges: 3
Edges: [C -> A, B -> C, A -> B]
```

### 2. Add nodes

```java
manager.addNode("NewNode");
manager.addNodes(new String[]{"Node1", "Node2", "Node3"});
```

#### Output:
```
Adding nodes and edges...
Added node 'NewNode': true
Added nodes: Node1, Node2
```

### 3. Add edges

```java
manager.addEdge("Node1", "Node2");
```

#### Output:
```
Added edge 'Node1' -> 'Node2': true
```
### 4. Output to DOT file and PNG

```java
manager.outputDOTGraph("path/to/output.dot");
manager.outputGraphics("path/to/output.png", "png");
```

#### Output:
```
Outputting to DOT and PNG...
DOT output:
digraph {
  "Node1" -> "Node2"
  "A" -> "B"
  "C" -> "A"
  "B" -> "C"
}
Successfully output graph to output.dot
Graphics output saved to: output.png in PNG format
Graphics output saved to: output.png
```
#### Output PNG:
![Output Graph](image.png)

## New Features in Part 2

### 5. Remove Nodes and Edges

```java
// Remove a single node
manager.removeNode("Node1");

// Remove multiple nodes
manager.removeNodes(new String[]{"Node2", "Node3"});

// Remove an edge
manager.removeEdge("A", "B");
```

#### Example Output:
```java
=== Creating Initial Graph ===
Initial Graph:
Number of nodes: 4
Node labels: [C, D, B, A]
Number of edges: 4
Edges: [A -> B, A -> D, C -> D, B -> C]


=== Removing Node 'B' ===
After removing node 'B':
Number of nodes: 3
Node labels: [A, C, D]
Number of edges: 2
Edges: [C -> D, A -> D]


=== Removing Edge A->D ===
After removing edge 'A->D':
Number of nodes: 3
Node labels: [C, A, D]
Number of edges: 1
Edges: [C -> D]


=== Attempting to remove non-existent node ===
```

### 6. Graph Search (BFS and DFS)

```java
// Search using BFS
GraphPath bfsPath = manager.GraphSearch("A", "D", Algorithm.BFS);

// Search using DFS
GraphPath dfsPath = manager.GraphSearch("A", "D", Algorithm.DFS);

// Print the found paths
System.out.println("BFS Path: " + bfsPath);  // Output: A -> B -> C -> D
System.out.println("DFS Path: " + dfsPath);  // Output: A -> D
```

#### Example Output:

```java
=== Creating Graph for Path Finding ===
Graph Structure:
Number of nodes: 5
Node labels: [D, C, A, E, B]
Number of edges: 5
Edges: [A -> B, D -> E, B -> C, A -> E, C -> D]


=== Finding path from A to E using BFS ===
BFS Path: A -> E

=== Finding path from A to E using DFS ===
DFS Path: A -> B -> C -> D -> E

=== Finding path in cyclic graph ===
Cyclic Graph Structure:
Number of nodes: 3
Node labels: [A, C, B]
Number of edges: 3
Edges: [C -> A, B -> C, A -> B]

BFS Path A->C: A -> B -> C
DFS Path A->C: A -> B -> C
```

## Continuous Integration

This project uses GitHub Actions for continuous integration. Every push to the repository automatically:
- Builds the project
- Runs all tests
- Reports test results

![Output Graph](actions.png)

## Running Tests

To run the tests, use the following command:

```
mvn test
```

## GitHub Commits

### Part 1 Features
- Parse Graph: [6864b69](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/6864b69c6fa51dcfd7ef4598b6bc5cac67fdde6d)
- Add Nodes: [c75c667](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/c75c667bb9b83adebc6025f6f72e8a6fe96753ff)
- Add Edges: [86ce05d](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/86ce05d49ef704968baabd443a605c1d84879ed1)
- Output Graph: [29c02e6](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/29c02e67cb34e89810a41d813f69a65061f42bab)

### Part 2 Features
- Remove APIs: [4fa5ea5](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/4fa5ea58db38933d6b0222782fde26a24c6619ee)
- CI Setup: [58e0e20](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/58e0e20e93b4da7f0b6129b146432a81fb15839f)
- BFS Implementation: [2cea339](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/2cea339c5b3fce6273b5a461623641b3bbd714f1)
- DFS Implementation: [3c43d5a](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/3c43d5aaafd74f92448263093ca1bd7e44047e8a)
- Algorithm Selection: [a3e4f7c](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/a3e4f7cbc13ea547d62d1e3796486668c70c5fd1)

### Branches
- BFS Branch: [bfs](https://github.com/satyalyadav/CSE-464-2024-syadav42/tree/bfs)
- DFS Branch: [dfs](https://github.com/satyalyadav/CSE-464-2024-syadav42/tree/dfs)

### Merges
- BFS Merge: [bf55837](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/bf55837be34316b4ff6b29d5a5c9a9d696309fd5)
- DFS Merge with Algorithm Selection: [a3e4f7c](https://github.com/satyalyadav/CSE-464-2024-syadav42/commit/a3e4f7cbc13ea547d62d1e3796486668c70c5fd1)