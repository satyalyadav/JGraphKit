import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.*;

public class RandomWalkSearchStrategy implements SearchStrategy {
    private static final int MAX_STEPS = 1000; // Prevent infinite loops

    @Override
    public GraphPath findPath(MutableGraph graph, String srcLabel, String dstLabel) {
        Random random = new Random();
        Map<String, String> parentMap = new HashMap<>();
        GraphPath currentPath = new GraphPath();
        
        // Find source and destination nodes
        MutableNode current = findNode(graph, srcLabel);
        MutableNode dst = findNode(graph, dstLabel);
        
        if (current == null || dst == null) {
            throw new IllegalArgumentException("Source or destination node does not exist");
        }

        currentPath.addNode(srcLabel);
        System.out.println("random testing");
        System.out.println("visiting " + currentPath);

        int steps = 0;
        while (steps < MAX_STEPS) {
            String currentLabel = current.name().toString();
            
            // Check if we reached destination
            if (currentLabel.equals(dstLabel)) {
                return currentPath;
            }
            
            // Get all neighbors
            List<MutableNode> neighbors = new ArrayList<>();
            for (Link link : current.links()) {
                String neighborLabel = link.to().name().toString();
                MutableNode neighbor = findNode(graph, neighborLabel);
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
            }
            
            // No neighbors to explore
            if (neighbors.isEmpty()) {
                return null;
            }
            
            // Randomly select next node
            MutableNode next = neighbors.get(random.nextInt(neighbors.size()));
            String nextLabel = next.name().toString();
            
            // Update path
            parentMap.put(nextLabel, currentLabel);
            currentPath.addNode(nextLabel);
            System.out.println("visiting " + currentPath);
            
            current = next;
            steps++;
        }
        
        // If we exceed max steps, return null
        return null;
    }
    
    private MutableNode findNode(MutableGraph graph, String label) {
        return graph.nodes().stream()
                .filter(n -> n.name().toString().equals(label))
                .findFirst()
                .orElse(null);
    }
}