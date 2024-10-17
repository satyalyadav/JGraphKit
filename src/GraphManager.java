import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GraphManager {
    private MutableGraph graph;

    // Feature 1: Parse a DOT graph file to create a graph
    public void parseGraph(String filepath) throws IOException {
        File dotFile = new File(filepath);
        graph = new Parser().read(dotFile);
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
        sb.append("Node labels: ").append(graph.nodes()).append("\n");
        sb.append("Number of edges: ").append(getEdgeCount()).append("\n");
        sb.append("Edges: ").append(graph.edges()).append("\n");
        return sb.toString();
    }

    // Feature 1: Output graph to file
    public void outputGraph(String filepath) throws IOException {
        File outputFile = new File(filepath);
        Graphviz.fromGraph(graph).render(Format.DOT).toFile(outputFile);
    }

    private MutableNode getMutableNode(String label) {
        return graph.nodes().stream()
                .filter(n -> n.name().value().equals(label))
                .findFirst()
                .orElseGet(() -> {
                    MutableNode newNode = graph.mutNode(label);
                    graph.add(newNode);
                    return newNode;
                });
    }
}