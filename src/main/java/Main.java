import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            GraphManager manager = new GraphManager();

            // Parse an existing DOT file
            manager.parseGraph("path/to/your/input.dot");

            // Print graph information
            System.out.println(manager);

            // Add nodes and edges
            manager.addNode("NewNode");
            manager.addNodes(new String[]{"Node1", "Node2"});
            manager.addEdge("Node1", "Node2");

            // Output to DOT and PNG
            manager.outputDOTGraph("path/to/output.dot");
            manager.outputGraphics("path/to/output.png", "png");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}