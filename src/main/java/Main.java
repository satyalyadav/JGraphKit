public class Main {
    public static void main(String[] args) {
        GraphManager manager = new GraphManager();

        // Parse input.dot from resources
        String filepath = "src/test/resources/input.dot";
        System.out.println("Parsing file: " + filepath);
        boolean parseSuccess = manager.parseGraph(filepath);
        if (!parseSuccess) {
            System.out.println("Failed to parse input.dot");
            return;
        }

        // Print initial graph information
        System.out.println("\nInitial Graph Information:");
        System.out.println(manager);

        // Test random walk search multiple times
        System.out.println("\nTesting Random Walk Search from 'a' to 'c':");
        for (int i = 0; i < 3; i++) {
            System.out.println("\nAttempt " + (i + 1) + ":");
            GraphPath path = manager.searchPath("a", "c", Algorithm.RANDOM_WALK);
            if (path != null) {
                System.out.println("Found path: " + path);
            } else {
                System.out.println("No path found");
            }
            System.out.println("--------------------");
        }
    }
}