import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;
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
    
        boolean edgeExists = graph.edges().stream()
            .anyMatch(e -> {
                boolean sameSource = e.from().name().toString().equals(srcLabel);
                boolean sameTarget = e.to().name().toString().equals(dstLabel);
                return sameSource && sameTarget;
            });
    
        if (!edgeExists) {
            src.addLink(dst);
            return true;
        }
        return false;
    }

    // Feature 4: Output the graph to a DOT file
    public boolean outputDOTGraph(String path) {
        try {
            String dotContent = GraphFileHandler.generateDOTFormat(graph);
            GraphFileHandler.writeToFile(dotContent, path);
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

    private boolean shouldKeepNode(String nodeName, String[] nodesToRemove) {
        for (String label : nodesToRemove) {
            if (nodeName.equals(label)) {
                return false;
            }
        }
        return true;
    }
    
    private MutableGraph createNewGraphWithoutNodes(String[] nodesToRemove) {
        MutableGraph newGraph = mutGraph("graph").setDirected(true);
        
        // Copy all nodes except those to be removed
        for (MutableNode node : graph.nodes()) {
            String nodeName = node.name().toString();
            if (shouldKeepNode(nodeName, nodesToRemove)) {
                newGraph.add(mutNode(nodeName));
            }
        }
        
        // Copy all edges except those connected to removed nodes
        for (MutableNode node : graph.nodes()) {
            String nodeName = node.name().toString();
            if (shouldKeepNode(nodeName, nodesToRemove)) {
                for (Link link : node.links()) {
                    String targetName = link.to().name().toString();
                    if (shouldKeepNode(targetName, nodesToRemove)) {
                        getOrCreateNode(newGraph, nodeName)
                            .addLink(getOrCreateNode(newGraph, targetName));
                    }
                }
            }
        }
        
        return newGraph;
    }
    
    // Feature: Remove single node
    public boolean removeNode(String label) {
        String[] nodesToRemove = {label};
        boolean nodeExists = graph.nodes().stream()
                .anyMatch(n -> n.name().toString().equals(label));
        
        this.graph = createNewGraphWithoutNodes(nodesToRemove);
        return nodeExists;
    }
    
    // Feature: Remove multiple nodes
    public void removeNodes(String[] labels) {
        this.graph = createNewGraphWithoutNodes(labels);
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

    public GraphPath searchPath(String srcLabel, String dstLabel, Algorithm algo) {
        GraphSearchAlgorithm searchAlgorithm = switch (algo) {
            case BFS -> new BFSTemplate(graph);
            case DFS -> new DFSTemplate(graph);
        };
        
        return searchAlgorithm.findPath(srcLabel, dstLabel);
    }
}