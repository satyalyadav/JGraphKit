import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.*;

public class BFSSearchStrategy implements SearchStrategy {
    @Override
    public GraphPath findPath(MutableGraph graph, String srcLabel, String dstLabel) {
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Queue<MutableNode> queue = new LinkedList<>();
        
        // Find source and destination nodes
        MutableNode src = findNode(graph, srcLabel);
        MutableNode dst = findNode(graph, dstLabel);
        
        if (src == null || dst == null) {
            throw new IllegalArgumentException("Source or destination node does not exist");
        }
        
        queue.offer(src);
        visited.add(srcLabel);
        
        while (!queue.isEmpty()) {
            MutableNode current = queue.poll();
            String currentLabel = current.name().toString();
            
            if (currentLabel.equals(dstLabel)) {
                return constructPath(dstLabel, parentMap);
            }
            
            for (Link link : current.links()) {
                String neighborLabel = link.to().name().toString();
                if (!visited.contains(neighborLabel)) {
                    MutableNode neighbor = findNode(graph, neighborLabel);
                    if (neighbor != null) {
                        queue.offer(neighbor);
                        visited.add(neighborLabel);
                        parentMap.put(neighborLabel, currentLabel);
                    }
                }
            }
        }
        return null;
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