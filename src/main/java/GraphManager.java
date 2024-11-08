import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class GraphManager {
    private MutableGraph graph;

    public GraphManager() {
        this.graph = mutGraph("graph").setDirected(true);
    }

    // Feature 1: Parse a DOT graph file to create a graph
    public boolean parseGraph(String filepath) {
        try {
            File dotFile = new File(filepath);
            System.out.println("Parsing file: " + filepath);
            this.graph = new Parser().read(dotFile);
            System.out.println("Parsed graph: " + this.graph);
            return true;
        } catch (IOException e) {
            System.err.println("Error parsing graph: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to get the number of nodes
    public int getNodeCount() {
        return graph.nodes().size();
    }

    // Helper method to get the number of edges
    public int getEdgeCount() {
        return graph.edges().size();
    }

    // Feature 1: Output graph information
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of nodes: ").append(getNodeCount()).append("\n");
        sb.append("Node labels: ").append(graph.nodes().stream()
                .map(n -> n.name().toString())
                .collect(Collectors.toList())).append("\n");
        sb.append("Number of edges: ").append(getEdgeCount()).append("\n");
        sb.append("Edges: ").append(graph.edges().stream()
                .map(e -> e.from().name() + " -> " + e.to().name())
                .collect(Collectors.toList())).append("\n");
        return sb.toString();
    }

    // Feature 1: Output graph to file
    public void outputGraph(String filepath) throws IOException {
        File outputFile = new File(filepath);
        Graphviz.fromGraph(graph).render(Format.DOT).toFile(outputFile);
    }

    // Feature 2: Add a single node
    public boolean addNode(String label) {
        if (graph.nodes().stream().noneMatch(n -> n.name().toString().equals(label))) {
            graph.add(mutNode(label));
            return true;
        }
        return false;
    }

    // Feature 2: Add multiple nodes
    public void addNodes(String[] labels) {
        for (String label : labels) {
            addNode(label);
        }
    }

    // Feature 3: Add an edge
    public boolean addEdge(String srcLabel, String dstLabel) {
        MutableNode src = getOrCreateNode(srcLabel);
        MutableNode dst = getOrCreateNode(dstLabel);

        if (graph.edges().stream().noneMatch(e ->
                e.from().name().toString().equals(srcLabel) &&
                        e.to().name().toString().equals(dstLabel))) {
            src.addLink(dst);
            return true;
        }
        return false;
    }

    // Feature 4: Output the graph to a DOT file
    public boolean outputDOTGraph(String path) {
        try {
            File outputFile = new File(path);
            StringBuilder dotFormat = new StringBuilder("digraph {\n");
            for (MutableNode node : graph.nodes()) {
                for (Link link : node.links()) {
                    dotFormat.append(String.format("  \"%s\" -> \"%s\"\n", node.name().toString(), link.to().name().toString()));
                }
            }
            dotFormat.append("}\n");

            String dotContent = dotFormat.toString();
            Files.writeString(outputFile.toPath(), dotContent);
            System.out.println("DOT output:\n" + dotContent);
            return true;
        } catch (IOException e) {
            System.err.println("Error outputting DOT graph: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Feature 4: Output the graph to a graphics file
    public void outputGraphics(String path, String format) throws IOException {
        if (!format.equalsIgnoreCase("png")) {
            throw new IllegalArgumentException("Unsupported format. Only PNG is supported.");
        }

        File outputFile = new File(path);
        Graphviz.fromGraph(graph).render(Format.PNG).toFile(outputFile);
        System.out.println("Graphics output saved to: " + path + " in PNG format");
    }

    private MutableNode getOrCreateNode(String label) {
        return graph.nodes().stream()
                .filter(n -> n.name().toString().equals(label))
                .findFirst()
                .orElseGet(() -> {
                    MutableNode newNode = mutNode(label);
                    graph.add(newNode);
                    return newNode;
                });
    }

    // Feature: Remove a single node
    public boolean removeNode(String label) {
        // Create a new graph
        MutableGraph newGraph = mutGraph("graph").setDirected(true);
        boolean nodeFound = false;
        
        // Copy all nodes except the one to be removed
        for (MutableNode node : graph.nodes()) {
            if (!node.name().toString().equals(label)) {
                MutableNode newNode = mutNode(node.name().toString());
                newGraph.add(newNode);
            } else {
                nodeFound = true;
            }
        }
        
        // Copy all edges except those connected to the removed node
        for (MutableNode node : graph.nodes()) {
            String nodeName = node.name().toString();
            if (!nodeName.equals(label)) {
                for (Link link : node.links()) {
                    String targetName = link.to().name().toString();
                    if (!targetName.equals(label)) {
                        getOrCreateNode(newGraph, nodeName)
                            .addLink(getOrCreateNode(newGraph, targetName));
                    }
                }
            }
        }
        
        this.graph = newGraph;
        return nodeFound;
    }

    // Feature: Remove multiple nodes
    public void removeNodes(String[] labels) {
        // Create a new graph
        MutableGraph newGraph = mutGraph("graph").setDirected(true);
        
        // Copy all nodes except those to be removed
        for (MutableNode node : graph.nodes()) {
            String nodeName = node.name().toString();
            boolean shouldKeep = true;
            for (String label : labels) {
                if (nodeName.equals(label)) {
                    shouldKeep = false;
                    break;
                }
            }
            if (shouldKeep) {
                newGraph.add(mutNode(nodeName));
            }
        }
        
        // Copy all edges except those connected to removed nodes
        for (MutableNode node : graph.nodes()) {
            String nodeName = node.name().toString();
            boolean srcShouldKeep = true;
            for (String label : labels) {
                if (nodeName.equals(label)) {
                    srcShouldKeep = false;
                    break;
                }
            }
            
            if (srcShouldKeep) {
                for (Link link : node.links()) {
                    String targetName = link.to().name().toString();
                    boolean dstShouldKeep = true;
                    for (String label : labels) {
                        if (targetName.equals(label)) {
                            dstShouldKeep = false;
                            break;
                        }
                    }
                    
                    if (dstShouldKeep) {
                        getOrCreateNode(newGraph, nodeName)
                            .addLink(getOrCreateNode(newGraph, targetName));
                    }
                }
            }
        }
        
        this.graph = newGraph;
    }

    // Feature: Remove an edge
    public boolean removeEdge(String srcLabel, String dstLabel) {
        MutableNode src = graph.nodes().stream()
                .filter(n -> n.name().toString().equals(srcLabel))
                .findFirst()
                .orElse(null);

        if (src == null) {
            throw new IllegalArgumentException("Source node does not exist: " + srcLabel);
        }

        MutableNode dst = graph.nodes().stream()
                .filter(n -> n.name().toString().equals(dstLabel))
                .findFirst()
                .orElse(null);

        if (dst == null) {
            throw new IllegalArgumentException("Destination node does not exist: " + dstLabel);
        }

        return src.links().removeIf(link -> 
            link.to().name().toString().equals(dstLabel));
    }

    // Helper method for node creation in new graph
    private MutableNode getOrCreateNode(MutableGraph g, String label) {
        return g.nodes().stream()
                .filter(n -> n.name().toString().equals(label))
                .findFirst()
                .orElseGet(() -> {
                    MutableNode newNode = mutNode(label);
                    g.add(newNode);
                    return newNode;
                });
    }

    // Feature: BFS Graph Search
    public GraphPath GraphSearch(String srcLabel, String dstLabel) {
        // Verify both nodes exist
        MutableNode src = graph.nodes().stream()
                .filter(n -> n.name().toString().equals(srcLabel))
                .findFirst()
                .orElse(null);

        MutableNode dst = graph.nodes().stream()
                .filter(n -> n.name().toString().equals(dstLabel))
                .findFirst()
                .orElse(null);

        if (src == null || dst == null) {
            throw new IllegalArgumentException("Source or destination node does not exist");
        }

        // BFS implementation
        Queue<MutableNode> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(src);
        visited.add(srcLabel);
        
        while (!queue.isEmpty()) {
            MutableNode current = queue.poll();
            String currentLabel = current.name().toString();
            
            // If we've reached the destination
            if (currentLabel.equals(dstLabel)) {
                // Construct the path
                GraphPath path = new GraphPath();
                String node = dstLabel;
                while (node != null) {
                    path.addNode(node);
                    node = parentMap.get(node);
                }
                
                // Reverse the path (since we built it backwards)
                GraphPath finalPath = new GraphPath();
                List<String> nodes = path.getNodes();
                for (int i = nodes.size() - 1; i >= 0; i--) {
                    finalPath.addNode(nodes.get(i));
                }
                return finalPath;
            }
            
            // Add unvisited neighbors to queue
            for (Link link : current.links()) {
                String neighborLabel = link.to().name().toString();
                if (!visited.contains(neighborLabel)) {
                    // Find the actual MutableNode for the neighbor
                    MutableNode neighbor = graph.nodes().stream()
                            .filter(n -> n.name().toString().equals(neighborLabel))
                            .findFirst()
                            .orElse(null);
                    
                    if (neighbor != null) {
                        queue.offer(neighbor);
                        visited.add(neighborLabel);
                        parentMap.put(neighborLabel, currentLabel);
                    }
                }
            }
        }
        
        // No path found
        return null;
    }
}