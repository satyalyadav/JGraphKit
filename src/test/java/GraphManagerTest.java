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

        assertTrue(graphManager.parseGraph(dotFile.toString()), "Parsing should succeed");
        String result = graphManager.toString();
        System.out.println("Parsed graph result:\n" + result);

        assertTrue(result.contains("Number of nodes: 3"), "Should have 3 nodes");
        assertTrue(result.contains("Node labels: [a, b, c]"), "Should have correct node labels");
        assertTrue(result.contains("Number of edges: 2"), "Should have 2 edges");
        assertTrue(result.contains("Edges: [a -> b, b -> c]"), "Should have correct edges");
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
        assertTrue(graphManager.outputDOTGraph(outputFile.toString()), "Output should succeed");

        String content = Files.readString(outputFile);
        System.out.println("Output DOT file content:\n" + content);

        assertTrue(content.contains("digraph"), "Should contain 'digraph'");
        assertTrue(content.contains("A"), "Should contain 'A'");
        assertTrue(content.contains("B"), "Should contain 'B'");
        assertTrue(content.contains("->"), "Should contain '->'");

        // Use a more flexible regular expression to check for "A" -> "B"
        String regex = ".*\"A\"\\s*->\\s*\"B\".*";
        assertTrue(content.replaceAll("\\s+", " ").matches(regex),
                "Should contain 'A -> B' (allowing for whitespace and newlines)");

        // Print debug information if the assertion fails
        if (!content.replaceAll("\\s+", " ").matches(regex)) {
            System.out.println("Regex: " + regex);
            System.out.println("Content (whitespace normalized): " + content.replaceAll("\\s+", " "));
        }
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