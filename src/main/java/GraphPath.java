import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GraphPath {
    private List<String> nodes;
    
    public GraphPath() {
        this.nodes = new ArrayList<>();
    }
    
    public void addNode(String node) {
        nodes.add(node);
    }
    
    public List<String> getNodes() {
        return new ArrayList<>(nodes);
    }
    
    @Override
    public String toString() {
        return nodes.stream().collect(Collectors.joining(" -> "));
    }
    
    // Copy constructor for creating new paths
    public GraphPath(GraphPath other) {
        this.nodes = new ArrayList<>(other.nodes);
    }
}