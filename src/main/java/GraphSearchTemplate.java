import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.*;

public abstract class GraphSearchTemplate {
    protected final MutableGraph graph;
    
    public GraphSearchTemplate(MutableGraph graph) {
        this.graph = graph;
    }
    
    // Template method defining the algorithm structure
    public final GraphPath findPath(String srcLabel, String dstLabel) {
        // Validate input
        MutableNode src = findNode(srcLabel);
        MutableNode dst = findNode(dstLabel);
        if (src == null || dst == null) {
            throw new IllegalArgumentException("Source or destination node does not exist");
        }
        
        // Initialize tracking structures
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        // Execute search
        boolean found = executeSearch(src, srcLabel, dstLabel, visited, parentMap);
        
        // Construct path if found
        return found ? constructPath(dstLabel, parentMap) : null;
    }
    
    // Abstract methods to be implemented by concrete classes
    protected abstract boolean executeSearch(MutableNode start, String startLabel, 
                                          String targetLabel, Set<String> visited, 
                                          Map<String, String> parentMap);
    
    // Common operations
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
        
        // Reverse the path to get correct order
        GraphPath finalPath = new GraphPath();
        List<String> nodes = path.getNodes();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            finalPath.addNode(nodes.get(i));
        }
        return finalPath;
    }
}