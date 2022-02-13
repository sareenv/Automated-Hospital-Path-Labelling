
package MAIN;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;




class Graph {

    static class Edge {
        int src;
        int dest;

        public Edge(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }

        @Override
        public String toString() {
            return "Edge (" +
                    "src=" + src +
                    ", dest=" + dest + " )";
        }
    }

    private HashSet<Integer> vertices;
    private ArrayList[] graph;

    public Graph(int vertexCount) {
        this.graph = new ArrayList[vertexCount];
        vertices = new HashSet<Integer>();
        for (int i = 0; i< vertexCount; i++) {
            this.graph[i] = new ArrayList();
        }
    }

    // creates the edges between the src and dest and adds to the graph G;
    public void addEdge(int src, int destination) {
        Edge edge = new Edge(src, destination);
        this.graph[src].add(edge);
    }

    public void loadContent() throws FileNotFoundException {
        String path = "/Users/databunker/IdeaProjects/HospitalPathLabelling/src/MAIN";
        String fileName = "/Contents2.txt";
        String overAllPath = path + fileName;
        File file = new File(overAllPath);
        Scanner snc = new Scanner(file);
        while (snc.hasNext()) {
            String input = snc.nextLine();
            String[] tokens = input.split(",");
            int srcToken = Integer.parseInt(tokens[0]);
            int destToken = Integer.parseInt(tokens[1]);
            this.vertices.add(srcToken);
            this.vertices.add(destToken);
        }

        snc.close();

        // Initialised the scanner again to get the valid input from the user.
        snc = new Scanner(file);
        while (snc.hasNext()) {
            String input = snc.nextLine();
            String[] tokens = input.split(",");
            int srcToken = Integer.parseInt(tokens[0]);
            int destToken = Integer.parseInt(tokens[1]);
            this.addEdge(srcToken, destToken);
        }
        snc.close();
    }



    /*
    * Is graph connected.
    * */

    boolean isGraphConnected() {
        ArrayList<Object> result = printPathAugmentation();
        ArrayList<String> cachePath = (ArrayList<String>) result.get(0);
        if (cachePath.size() == 0) { return false; }
        return true;
    }

    /*
    *  - Bridge Detection Algorithms for the graph.
    *       - To detect if the bridge exist or not in the graph remove one
    *           edge and check if the graph is still connected or not.
    * */

    public boolean bridgeDetector(ArrayList<Edge>[] graph) {
        return false;
    }


    public void printGraph() {
        for (int i = 0; i< this.vertices.size(); i++) {
            System.out.println( i + ": " + Arrays.toString(graph[i].toArray()));
        }
    }

    // generates the hamiltonian path from the src vertex to the destination vertex.
    public void pathsAugmentation(ArrayList<Edge>[] graph, int src, HashSet<Integer> visited,
                                  String psf,
                                  ArrayList<String> cache,
                                  int[] order, int prevOrder) {
        if (visited.size()
                == graph.length - 1) {
            order[src] = order[prevOrder - 1] + 1;
            cache.add(psf);
            return;
        }
        visited.add(src);

        if (src == 0) {
            order[src] = 1;
        } else {
            order[src] = order[src - 1] + 1;
        }

        for (Edge e: graph[src]) {
            if (!visited.contains(e.dest)) {
                pathsAugmentation(graph, e.dest, visited,
                        psf + e.dest, cache, order, prevOrder + 1);
            }
        }
        visited.remove(src);
    }

    ArrayList<Object> printPathAugmentation() {
        // paths augmentation.
        Graph g = this;
        System.out.println("Augmenting the paths from the src node 0 " +
                "to all the other paths in the graph.");
        HashSet<Integer> visited = new HashSet<>();
        ArrayList<String> cache = new ArrayList<>();
        int[] order = new int[g.vertices.size()];
        g.pathsAugmentation(g.graph, 0, visited,  0 + "",
                cache , order, 0);
        for (String p: cache) {
            System.out.println(p);
        }
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(cache);
        objs.add(order);
        return objs;
    }


    static class AugmentedPair {
        char nodeA;
        char nodeB;


        public AugmentedPair(char nodeA, char nodeB) {
            this.nodeA = nodeA;
            this.nodeB = nodeB;
        }


        @Override
        public String toString() {
            return "AugmentedPair{" +
                    "nodeA=" + nodeA +
                    ", nodeB=" + nodeB +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AugmentedPair pair = (AugmentedPair) o;
            return nodeA == pair.nodeA && nodeB == pair.nodeB;
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeA, nodeB);
        }
    }


    // labelling Algorithm - Works based on the logic mentioned in the book.
    public static void labellingLogic(ArrayList<AugmentedPair> uniquePairs, int[] order) {
        ArrayList<AugmentedPair> visitedEdges = new ArrayList<>();
        HashSet<String> visitedVertices = new HashSet<>();

        for (AugmentedPair pair: uniquePairs) {
            // skip if the edge has been already marked.
            if (visitedEdges.contains(pair)) {
                continue;
            }

            int nodeAInt = Integer.parseInt(pair.nodeA + "");
            int nodeBInt = Integer.parseInt(pair.nodeB + "");

            if(order[nodeAInt] < order[nodeBInt] &&
                    !visitedVertices.contains(nodeBInt + "")) {
                System.out.println(nodeAInt + " -> " + nodeBInt);
                visitedEdges.add(pair);
                visitedVertices.add(nodeBInt + "");
                visitedVertices.add(nodeAInt + "");
            }

            else if (order[nodeAInt] < order[nodeBInt]
                    && visitedVertices.contains(nodeBInt + "")) {
                System.out.println(nodeBInt + " -> " + nodeAInt);
                visitedEdges.add(pair);
                visitedVertices.add(nodeAInt + "");
            }
        }
    }

    // String processing utility
    ArrayList<AugmentedPair> generateAugmentedPairs(ArrayList<String> cache) {
        ArrayList<AugmentedPair> uniquePairs = new ArrayList<>();
        for (String path: cache) {
            for (int i = 0; i < path.length() - 1; i++) {
                char c1 = path.charAt(i);
                char c2 = path.charAt(i + 1);
                AugmentedPair pair = new AugmentedPair(c1, c2);
                if (!uniquePairs.contains(pair)) {
                    uniquePairs.add(pair);
                }
            }
        }
        return uniquePairs;
    }

    public static void main(String[] args) {
        System.out.println("Please enter the vertex count in the source file");
        Scanner snc = new Scanner(System.in);
        int graphCount = snc.nextInt();
        snc.close();
        Graph g = new Graph(graphCount);
        try {
            g.loadContent();
            g.printGraph();
            // path augmentation shows that there
            // exist a path between src vertex
            // to all the other vertex which shows that the graph is connected !.
            if (!g.isGraphConnected()) {
                System.out.println("Sorry this graph is not connected and " +
                        "therefore cannot be labelled by algorithm for one way street problem");
                return;
            }
            ArrayList<Object> objs = g.printPathAugmentation();
            int[] order = (int[]) objs.get(1);
            ArrayList<String> cache = (ArrayList<String>) objs.get(0);
            ArrayList<AugmentedPair> augmentedPairs =  g.generateAugmentedPairs(cache);
            System.out.println("Path labelling for One Way Street Problem ᧠. " +
                    "Mark the Labelling in the following fashion");
            labellingLogic(augmentedPairs, order);
        } catch (FileNotFoundException e) {
            System.out.println("File opening exception! Whoops check the " +
                    "text file containing the graph information is " +
                    "loaded properly into the program");
        }
    }
}