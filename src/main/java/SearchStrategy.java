import guru.nidi.graphviz.model.MutableGraph;

public interface SearchStrategy {
    GraphPath findPath(MutableGraph graph, String srcLabel, String dstLabel);
}