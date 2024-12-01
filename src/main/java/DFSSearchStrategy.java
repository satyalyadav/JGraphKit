import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.*;

public class DFSSearchStrategy implements SearchStrategy {
    @Override
    public GraphPath findPath(MutableGraph graph, String srcLabel, String dstLabel) {
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        // Find source and destination nodes
        MutableNode src = findNode(graph, srcLabel);
        MutableNode dst = findNode(graph, dstLabel);
        
        if (src == null || dst == null) {
            throw new IllegalArgumentException("Source or destination node does not exist");
        }
        
        if (dfsSearch(graph, src, srcLabel, dstLabel, visited, parentMap)) {
            return constructPath(dstLabel, parentMap);
        }
        return null;
    }
    
    private boolean dfsSearch(MutableGraph graph, MutableNode current, String currentLabel, 
                            String dstLabel, Set<String> visited, Map<String, String> parentMap) {
        visited.add(currentLabel);
        
        if (currentLabel.equals(dstLabel)) {
            return true;
        }
        
        for (Link link : current.links()) {
            String neighborLabel = link.to().name().toString();
            if (!visited.contains(neighborLabel)) {
                MutableNode neighbor = findNode(graph, neighborLabel);
                if (neighbor != null) {
                    parentMap.put(neighborLabel, currentLabel);
                    if (dfsSearch(graph, neighbor, neighborLabel, dstLabel, visited, parentMap)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private MutableNode findNode(MutableGraph graph, String label) {
        return graph.nodes().stream()
                .filter(n -> n.name().toString().equals(label))
                .findFirst()
                .orElse(null);
    }
    
    private GraphPath constructPath(String dstLabel, Map<String, String> parentMap) {
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