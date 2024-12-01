import java.util.*;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

public class GraphPathFinder {
    private final MutableGraph graph;

    public GraphPathFinder(MutableGraph graph) {
        this.graph = graph;
    }

    public GraphPath findPath(String srcLabel, String dstLabel, Algorithm algo) {
        if (algo == null) {
            throw new IllegalArgumentException("Algorithm cannot be null");
        }

        MutableNode src = findNode(srcLabel);
        MutableNode dst = findNode(dstLabel);

        if (src == null || dst == null) {
            throw new IllegalArgumentException("Source or destination node does not exist");
        }

        return switch (algo) {
            case BFS -> bfsSearch(src, srcLabel, dstLabel);
            case DFS -> dfsSearch(src, srcLabel, dstLabel);
            case RANDOM_WALK -> randomWalkSearch(src, srcLabel, dstLabel);  // Add this case
        };
    }

    private MutableNode findNode(String label) {
        return graph.nodes().stream()
                .filter(n -> n.name().toString().equals(label))
                .findFirst()
                .orElse(null);
    }

    private GraphPath bfsSearch(MutableNode src, String srcLabel, String dstLabel) {
        Queue<MutableNode> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
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
                    MutableNode neighbor = findNode(neighborLabel);
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

    private GraphPath dfsSearch(MutableNode src, String srcLabel, String dstLabel) {
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        
        boolean found = dfsHelper(src, dstLabel, visited, parentMap);
        
        if (found) {
            return constructPath(dstLabel, parentMap);
        }
        return null;
    }

    private boolean dfsHelper(MutableNode current, String dstLabel, 
                            Set<String> visited, Map<String, String> parentMap) {
        String currentLabel = current.name().toString();
        visited.add(currentLabel);
        
        if (currentLabel.equals(dstLabel)) {
            return true;
        }
        
        for (Link link : current.links()) {
            String neighborLabel = link.to().name().toString();
            if (!visited.contains(neighborLabel)) {
                MutableNode neighbor = findNode(neighborLabel);
                
                if (neighbor != null) {
                    parentMap.put(neighborLabel, currentLabel);
                    if (dfsHelper(neighbor, dstLabel, visited, parentMap)) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    private GraphPath randomWalkSearch(MutableNode src, String srcLabel, String dstLabel) {
        Random random = new Random();
        Map<String, String> parentMap = new HashMap<>();
        GraphPath currentPath = new GraphPath();
        int MAX_STEPS = 1000;
        
        MutableNode current = src;
        currentPath.addNode(srcLabel);
        System.out.println("random testing");
        System.out.println("visiting " + currentPath);
    
        int steps = 0;
        while (steps < MAX_STEPS) {
            String currentLabel = current.name().toString();
            
            if (currentLabel.equals(dstLabel)) {
                return currentPath;
            }
            
            List<MutableNode> neighbors = new ArrayList<>();
            for (Link link : current.links()) {
                String neighborLabel = link.to().name().toString();
                MutableNode neighbor = findNode(neighborLabel);
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
            }
            
            if (neighbors.isEmpty()) {
                return null;
            }
            
            MutableNode next = neighbors.get(random.nextInt(neighbors.size()));
            String nextLabel = next.name().toString();
            
            parentMap.put(nextLabel, currentLabel);
            currentPath.addNode(nextLabel);
            System.out.println("visiting " + currentPath);
            
            current = next;
            steps++;
        }
        
        return null;
    }
}