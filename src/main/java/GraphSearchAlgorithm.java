import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.*;

public abstract class GraphSearchAlgorithm {
    protected final MutableGraph graph;
    
    public GraphSearchAlgorithm(MutableGraph graph) {
        this.graph = graph;
    }
    
    // Template method defining the skeleton algorithm
    public final GraphPath findPath(String srcLabel, String dstLabel) {
        // Step 1: Initialize
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        // Step 2: Validate and get source node (we only need start node for search)
        MutableNode src = validateAndGetNode(srcLabel);
        
        // We don't need to get dst node here, just validate it exists
        validateAndGetNode(dstLabel);  // Will throw exception if destination doesn't exist
        
        // Step 3: Initialize data structure
        initializeSearch(src, visited, parentMap);
        
        // Step 4: Search
        while (hasNextNode()) {
            MutableNode current = getNextNode();
            String currentLabel = current.name().toString();
            
            // Check if destination found
            if (currentLabel.equals(dstLabel)) {
                return constructPath(dstLabel, parentMap);
            }
            
            // Process neighbors
            processNeighbors(current, visited, parentMap);
        }
        
        return null;
    }
        
    // Abstract methods that must be implemented by subclasses
    protected abstract void initializeSearch(MutableNode start, Set<String> visited, 
                                          Map<String, String> parentMap);
    protected abstract boolean hasNextNode();
    protected abstract MutableNode getNextNode();
    protected abstract void processNeighbors(MutableNode current, Set<String> visited, 
                                           Map<String, String> parentMap);
    
    // Common methods shared by all implementations
    protected MutableNode validateAndGetNode(String label) {
        MutableNode node = findNode(label);
        if (node == null) {
            throw new IllegalArgumentException("Node does not exist: " + label);
        }
        return node;
    }
    
    protected MutableNode findNode(String label) {
        return graph.nodes().stream()
                .filter(n -> n.name().toString().equals(label))
                .findFirst()
                .orElse(null);
    }
    
    protected GraphPath constructPath(String dstLabel, Map<String, String> parentMap) {
        GraphPath path = new GraphPath();
        String node = dstLabel;
        while (node != null) {
            path.addNode(node);
            node = parentMap.get(node);
        }
        
        GraphPath finalPath = new GraphPath();
        List<String> nodes = path.getNodes();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            finalPath.addNode(nodes.get(i));
        }
        return finalPath;
    }
}