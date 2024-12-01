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
        System.out.println("Testing add node...");
        assertTrue(graphManager.addNode("NewNode"), "Should add new node");
        System.out.println("Added node: NewNode");
        assertFalse(graphManager.addNode("NewNode"), "Should not add duplicate node");
        System.out.println("Attempted to add duplicate node: NewNode (failed as expected)");
        assertEquals(1, graphManager.getNodeCount(), "Should have 1 node");
        System.out.println("Final node count: " + graphManager.getNodeCount());
    }

    @Test
    void testAddNodes() {
        System.out.println("Testing add multiple nodes...");
        graphManager.addNodes(new String[]{"Node1", "Node2", "Node3"});
        assertEquals(3, graphManager.getNodeCount(), "Should have 3 nodes");
        System.out.println("Added nodes: Node1, Node2, Node3");
        System.out.println("Final node count: " + graphManager.getNodeCount());
    }

    @Test
    void testAddEdge() {
        System.out.println("Testing add edge...");
        graphManager.addNode("A");
        graphManager.addNode("B");
        assertTrue(graphManager.addEdge("A", "B"), "Should add new edge");
        System.out.println("Added edge: A -> B");
        assertFalse(graphManager.addEdge("A", "B"), "Should not add duplicate edge");
        System.out.println("Attempted to add duplicate edge: A -> B (failed as expected)");
        assertEquals(1, graphManager.getEdgeCount(), "Should have 1 edge");
        System.out.println("Final edge count: " + graphManager.getEdgeCount());
    }

    @Test
    void testAddEdgeWithNonExistentNodes() {
        System.out.println("Testing add edge with non-existent nodes...");
        assertTrue(graphManager.addEdge("C", "D"), "Should add edge and create nodes");
        System.out.println("Added edge: C -> D (nodes created automatically)");
        assertEquals(2, graphManager.getNodeCount(), "Should have 2 nodes");
        assertEquals(1, graphManager.getEdgeCount(), "Should have 1 edge");
        System.out.println("Final node count: " + graphManager.getNodeCount());
        System.out.println("Final edge count: " + graphManager.getEdgeCount());
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

        String regex = ".*\"A\"\\s*->\\s*\"B\".*";
        assertTrue(content.replaceAll("\\s+", " ").matches(regex),
                "Should contain 'A -> B' (allowing for whitespace and newlines)");
    }

    @Test
    void testOutputGraphics() throws IOException {
        graphManager.addNode("A");
        graphManager.addNode("B");
        graphManager.addEdge("A", "B");

        Path outputFile = tempDir.resolve("output.png");
        graphManager.outputGraphics(outputFile.toString(), "png");

        assertTrue(Files.exists(outputFile), "PNG file should exist");
        assertTrue(Files.size(outputFile) > 0, "PNG file should not be empty");

        // Check if the file is actually a PNG
        byte[] pngHeader = Files.readAllBytes(outputFile);
        assertTrue(pngHeader.length >= 8 && pngHeader[0] == (byte)0x89 && pngHeader[1] == 0x50 && pngHeader[2] == 0x4E && pngHeader[3] == 0x47,
                "File should have PNG header");

        System.out.println("PNG file created successfully: " + outputFile);

        // Test unsupported format
        Path invalidFile = tempDir.resolve("output.jpg");
        assertThrows(IllegalArgumentException.class, () -> {
            graphManager.outputGraphics(invalidFile.toString(), "jpg");
        }, "Should throw IllegalArgumentException for unsupported format");
    }

    @Test
    void testRemoveNode() {
        System.out.println("Testing remove node...");
        graphManager.addNode("A");
        graphManager.addNode("B");
        graphManager.addEdge("A", "B");
        
        assertTrue(graphManager.removeNode("A"), "Should remove existing node");
        assertEquals(1, graphManager.getNodeCount(), "Should have 1 node remaining");
        assertFalse(graphManager.removeNode("A"), "Should return false for non-existent node");
        
        System.out.println("Node removal test completed successfully");
    }

    @Test
    void testRemoveNodes() {
        System.out.println("Testing remove multiple nodes...");
        graphManager.addNodes(new String[]{"A", "B", "C"});
        graphManager.addEdge("A", "B");
        graphManager.addEdge("B", "C");
        
        graphManager.removeNodes(new String[]{"A", "B"});
        assertEquals(1, graphManager.getNodeCount(), "Should have 1 node remaining");
        assertEquals(0, graphManager.getEdgeCount(), "Should have no edges remaining");
        
        System.out.println("Multiple nodes removal test completed successfully");
    }

    @Test
    void testRemoveEdge() {
        System.out.println("Testing remove edge...");
        graphManager.addNode("A");
        graphManager.addNode("B");
        graphManager.addEdge("A", "B");
        
        assertTrue(graphManager.removeEdge("A", "B"), "Should remove existing edge");
        assertEquals(0, graphManager.getEdgeCount(), "Should have no edges remaining");
        assertEquals(2, graphManager.getNodeCount(), "Should still have both nodes");
        
        System.out.println("Edge removal test completed successfully");
    }

    @Test
    void testRemoveEdgeExceptions() {
        System.out.println("Testing remove edge exceptions...");
        graphManager.addNode("A");
        
        assertThrows(IllegalArgumentException.class, () -> {
            graphManager.removeEdge("A", "NonExistent");
        }, "Should throw exception for non-existent destination node");
        
        assertThrows(IllegalArgumentException.class, () -> {
            graphManager.removeEdge("NonExistent", "A");
        }, "Should throw exception for non-existent source node");
        
        System.out.println("Edge removal exception tests completed successfully");
    }

    @Test
    void testSearchPathBFS() {
        setupTestGraph();
        
        // Test direct path with BFS
        GraphPath path1 = graphManager.searchPath("A", "D", Algorithm.BFS);
        assertNotNull(path1, "Should find a path from A to D");
        assertEquals("A -> D", path1.toString(), "BFS should find direct path from A to D");

        // Test longer path
        GraphPath path2 = graphManager.searchPath("A", "C", Algorithm.BFS);
        assertNotNull(path2, "Should find a path from A to C");
        assertEquals("A -> B -> C", path2.toString(), "BFS should find shortest path through B");

        // Test no path exists
        graphManager.addNode("E"); // Isolated node
        GraphPath path3 = graphManager.searchPath("A", "E", Algorithm.BFS);
        assertNull(path3, "Should return null when no path exists");

        // Test path to self
        GraphPath path4 = graphManager.searchPath("A", "A", Algorithm.BFS);
        assertNotNull(path4, "Should find a path from A to A");
        assertEquals("A", path4.toString(), "Path to self should only contain the node itself");
    }

    @Test
    void testSearchPathDFS() {
        setupTestGraph();
        
        // Test finding a path with DFS
        GraphPath path1 = graphManager.searchPath("A", "D", Algorithm.DFS);
        assertNotNull(path1, "Should find a path from A to D");
        assertTrue(
            path1.toString().equals("A -> D") || // Direct path
            path1.toString().equals("A -> B -> C -> D"), // Longer path through B and C
            "DFS should find a valid path from A to D"
        );

        // Test path to self
        GraphPath path2 = graphManager.searchPath("A", "A", Algorithm.DFS);
        assertNotNull(path2, "Should find a path from A to A");
        assertEquals("A", path2.toString(), "Path to self should only contain the node itself");

        // Test with no path available
        graphManager.addNode("E"); // Isolated node
        GraphPath path3 = graphManager.searchPath("A", "E", Algorithm.DFS);
        assertNull(path3, "Should return null when no path exists");
    }

    @Test
    void testSearchPathCyclic() {
        // Create a cyclic graph
        graphManager.addNode("A");
        graphManager.addNode("B");
        graphManager.addNode("C");
        graphManager.addEdge("A", "B");
        graphManager.addEdge("B", "C");
        graphManager.addEdge("C", "A");

        // Test both algorithms can handle cycles
        GraphPath bfsPath = graphManager.searchPath("A", "C", Algorithm.BFS);
        assertNotNull(bfsPath, "BFS should find a path in cyclic graph");
        assertTrue(bfsPath.getNodes().get(0).equals("A") && 
                  bfsPath.getNodes().get(bfsPath.getNodes().size() - 1).equals("C"),
                "BFS path should start at source and end at destination");

        GraphPath dfsPath = graphManager.searchPath("A", "C", Algorithm.DFS);
        assertNotNull(dfsPath, "DFS should find a path in cyclic graph");
        assertTrue(dfsPath.getNodes().get(0).equals("A") && 
                  dfsPath.getNodes().get(dfsPath.getNodes().size() - 1).equals("C"),
                "DFS path should start at source and end at destination");
    }

    @Test
    void testSearchPathInvalidInputs() {
        graphManager.addNode("A");
        
        assertThrows(IllegalArgumentException.class, () -> {
            graphManager.searchPath("A", "NonExistent", Algorithm.BFS);
        }, "Should throw exception for non-existent destination with BFS");
        
        assertThrows(IllegalArgumentException.class, () -> {
            graphManager.searchPath("NonExistent", "A", Algorithm.DFS);
        }, "Should throw exception for non-existent source with DFS");

        assertThrows(IllegalArgumentException.class, () -> {
            graphManager.searchPath("A", "A", null);
        }, "Should throw exception for null algorithm");
    }

    // Helper method to setup test graph
    private void setupTestGraph() {
        graphManager.addNode("A");
        graphManager.addNode("B");
        graphManager.addNode("C");
        graphManager.addNode("D");
        graphManager.addEdge("A", "B");
        graphManager.addEdge("B", "C");
        graphManager.addEdge("C", "D");
        graphManager.addEdge("A", "D"); // Direct path from A to D
    }
}