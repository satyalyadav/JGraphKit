import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            GraphManager manager = new GraphManager();

            // Parse an existing DOT file
            System.out.println("Parsing input.dot file...");
            boolean parseSuccess = manager.parseGraph("input.dot");
            if (parseSuccess) {
                System.out.println("Successfully parsed input.dot");
            } else {
                System.out.println("Failed to parse input.dot");
            }

            // Print initial graph information
            System.out.println("\nInitial Graph Information:");
            System.out.println(manager);

            // Add nodes and edges
            System.out.println("\nAdding nodes and edges...");
            boolean newNodeAdded = manager.addNode("NewNode");
            System.out.println("Added node 'NewNode': " + newNodeAdded);

            manager.addNodes(new String[]{"Node1", "Node2"});
            System.out.println("Added nodes: Node1, Node2");

            boolean edgeAdded = manager.addEdge("Node1", "Node2");
            System.out.println("Added edge 'Node1' -> 'Node2': " + edgeAdded);

            // Print updated graph information
            System.out.println("\nUpdated Graph Information:");
            System.out.println(manager);

            // Output to DOT and PNG
            System.out.println("\nOutputting to DOT and PNG...");
            boolean dotOutputSuccess = manager.outputDOTGraph("output.dot");
            if (dotOutputSuccess) {
                System.out.println("Successfully output graph to output.dot");
            } else {
                System.out.println("Failed to output graph to output.dot");
            }

            manager.outputGraphics("output.png", "png");
            System.out.println("Graphics output saved to: output.png");

            System.out.println("\nGraph processing completed successfully.");

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}