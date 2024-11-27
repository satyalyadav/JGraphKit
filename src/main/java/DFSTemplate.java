import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.*;

public class DFSTemplate extends GraphSearchAlgorithm {
    private Stack<MutableNode> stack;
    
    public DFSTemplate(MutableGraph graph) {
        super(graph);
    }
    
    @Override
    protected void initializeSearch(MutableNode start, Set<String> visited, 
                                  Map<String, String> parentMap) {
        stack = new Stack<>();
        stack.push(start);
        visited.add(start.name().toString());
    }
    
    @Override
    protected boolean hasNextNode() {
        return !stack.isEmpty();
    }
    
    @Override
    protected MutableNode getNextNode() {
        return stack.pop();
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
                    stack.push(neighbor);
                    visited.add(neighborLabel);
                    parentMap.put(neighborLabel, currentLabel);
                }
            }
        }
    }
}