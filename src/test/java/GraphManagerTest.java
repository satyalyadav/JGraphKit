import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GraphManagerTest {
    private GraphManager graphManager;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        graphManager = new GraphManager();
    }

    @Test
    void testParseGraph() throws IOException {
        // Create a sample DOT file
        Path dotFile = tempDir.resolve("input.dot");
        Files.writeString(dotFile, "digraph G { a -> b; b -> c; }");

        graphManager.parseGraph(dotFile.toString());
        String result = graphManager.toString();

        assertTrue(result.contains("Number of nodes: 3"));
        assertTrue(result.contains("Node labels: [a, b, c]"));
        assertTrue(result.contains("Number of edges: 2"));
        assertTrue(result.contains("Edges: [a -> b, b -> c]"));
    }

    @Test
    void testAddNode() {
        assertTrue(graphManager.addNode("NewNode"));
        assertFalse(graphManager.addNode("NewNode")); // Duplicate node
    }

    @Test
    void testAddNodes() {
        graphManager.addNodes(new String[]{"Node1", "Node2", "Node3"});
        assertEquals(3, graphManager.getNodeCount());
    }

    @Test
    void testAddEdge() {
        graphManager.addNode("A");
        graphManager.addNode("B");
        assertTrue(graphManager.addEdge("A", "B"));
        assertFalse(graphManager.addEdge("A", "B")); // Duplicate edge
    }

    @Test
    void testOutputDOTGraph() throws IOException {
        graphManager.addNode("A");
        graphManager.addNode("B");
        graphManager.addEdge("A", "B");

        Path outputFile = tempDir.resolve("output.dot");
        graphManager.outputDOTGraph(outputFile.toString());

        String content = Files.readString(outputFile);
        assertTrue(content.contains("digraph {"));
        assertTrue(content.contains("A -> B"));
    }

    @Test
    void testOutputGraphics() throws IOException {
        graphManager.addNode("A");
        graphManager.addNode("B");
        graphManager.addEdge("A", "B");

        Path outputFile = tempDir.resolve("output.png");
        graphManager.outputGraphics(outputFile.toString(), "png");

        assertTrue(Files.exists(outputFile));
        assertTrue(Files.size(outputFile) > 0);
    }
}