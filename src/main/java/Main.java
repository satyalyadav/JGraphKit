import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            GraphManager manager = new GraphManager();

            // Parse an existing DOT file
            manager.parseGraph("input.dot");

            // Print graph information
            System.out.println(manager);

            // Add nodes and edges
            manager.addNode("NewNode");
            manager.addNodes(new String[]{"Node1", "Node2"});
            manager.addEdge("Node1", "Node2");

            // Output to DOT and PNG
            manager.outputDOTGraph("output.dot");
            manager.outputGraphics("output.png", "png");

            System.out.println("Graph processing completed successfully.");

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}