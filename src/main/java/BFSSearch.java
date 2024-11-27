import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.*;

public class BFSSearch extends GraphSearchTemplate {
    public BFSSearch(MutableGraph graph) {
        super(graph);
    }
    
    @Override
    protected boolean executeSearch(MutableNode start, String startLabel, 
                                  String targetLabel, Set<String> visited, 
                                  Map<String, String> parentMap) {
        Queue<MutableNode> queue = new LinkedList<>();
        queue.offer(start);
        visited.add(startLabel);
        
        while (!queue.isEmpty()) {
            MutableNode current = queue.poll();
            String currentLabel = current.name().toString();
            
            if (currentLabel.equals(targetLabel)) {
                return true;
            }
            
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
        return false;
    }
}