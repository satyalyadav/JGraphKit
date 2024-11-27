import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.util.*;

public class DFSSearch extends GraphSearchTemplate {
    public DFSSearch(MutableGraph graph) {
        super(graph);
    }
    
    @Override
    protected boolean executeSearch(MutableNode start, String startLabel, 
                                  String targetLabel, Set<String> visited, 
                                  Map<String, String> parentMap) {
        visited.add(startLabel);
        String currentLabel = start.name().toString();
        
        if (currentLabel.equals(targetLabel)) {
            return true;
        }
        
        for (Link link : start.links()) {
            String neighborLabel = link.to().name().toString();
            if (!visited.contains(neighborLabel)) {
                MutableNode neighbor = findNode(neighborLabel);
                if (neighbor != null) {
                    parentMap.put(neighborLabel, currentLabel);
                    if (executeSearch(neighbor, neighborLabel, targetLabel, visited, parentMap)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}