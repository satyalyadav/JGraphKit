import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
}