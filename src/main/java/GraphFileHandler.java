import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

public class GraphFileHandler {
    public static void writeToFile(String content, String filePath) throws IOException {
        File outputFile = new File(filePath);
        Files.writeString(outputFile.toPath(), content);
    }

    public static String generateDOTFormat(MutableGraph graph) {
        StringBuilder dotFormat = new StringBuilder("digraph {\n");
        for (MutableNode node : graph.nodes()) {
            for (Link link : node.links()) {
                dotFormat.append(String.format("  \"%s\" -> \"%s\"\n", 
                    node.name().toString(), 
                    link.to().name().toString()));
            }
        }
        dotFormat.append("}\n");
        return dotFormat.toString();
    }
}