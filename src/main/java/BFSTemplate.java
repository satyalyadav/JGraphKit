import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.*;

public class BFSTemplate extends GraphSearchAlgorithm {
    private Queue<MutableNode> queue;
    
    public BFSTemplate(MutableGraph graph) {
        super(graph);
    }
    
    @Override
    protected void initializeSearch(MutableNode start, Set<String> visited, 
                                  Map<String, String> parentMap) {
        queue = new LinkedList<>();
        queue.offer(start);
        visited.add(start.name().toString());
    }
    
    @Override
    protected boolean hasNextNode() {
        return !queue.isEmpty();
    }
    
    @Override
    protected MutableNode getNextNode() {
        return queue.poll();
    }
    
    @Override
    protected void processNeighbors(MutableNode current, Set<String> visited, 
                                  Map<String, String> parentMap) {
        String currentLabel = current.name().toString();
        for (Link link : current.links()) {
            String neighborLabel = link.to().name().toString();
            if (!visited.contains(neighborLabel)) {
                MutableNode neighbor = findNode(neighborLabel);
                if (neighbor != null) {
                    queue.offer(neighbor);
                    visited.add(neighborLabel);
                    parentMap.put(neighborLabel, currentLabel);
                }
            }
        }
    }
}