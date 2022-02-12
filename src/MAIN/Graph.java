
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
        Gr.Edge edge;

        public AugmentedPair(char nodeA, char nodeB) {
            this.nodeA = nodeA;
            this.nodeB = nodeB;
            this.edge = null;
        }

        public AugmentedPair(char nodeA, char nodeB, Gr.Edge edge) {
            this.nodeA = nodeA;
            this.nodeB = nodeB;
            this.edge = edge;
        }

        @Override
        public String toString() {
            return "AugmentedPair{" +
                    "nodeA=" + nodeA +
                    ", nodeB=" + nodeB +
                    ", edge=" + edge +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AugmentedPair pair = (AugmentedPair) o;
            return nodeA == pair.nodeA && nodeB == pair.nodeB
                    && Objects.equals(edge, pair.edge);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeA, nodeB, edge);
        }
    }


    // labelling Algorithm - Works based on the logic mentioned in the book.
    public static void labellingLogic(ArrayList<AugmentedPair> uniquePairs, int[] order) {
        ArrayList<String> visitedNodes = new ArrayList<>();
        for (AugmentedPair pair: uniquePairs) {
            // wrong way of doing the labelling just based on the order of the nodes
            // and not considering the visited nodes to flip the signs in the process to doing it.
            int nodeAInt = Integer.parseInt(pair.nodeA + "");
            int nodeBInt = Integer.parseInt(pair.nodeB + "");
            if (order[nodeAInt] < order[nodeBInt] &&
                    !visitedNodes.contains(pair.nodeB + "")) {
                System.out.println(nodeAInt + " -> " + nodeBInt);
                visitedNodes.add(pair.nodeB + "");
                visitedNodes.add(pair.nodeA + "");

            } else if (order[nodeAInt] < order[nodeBInt]
                    && visitedNodes.contains(pair.nodeB + "")){
                System.out.println(nodeBInt + " -> " + nodeAInt);
            }
            // consider the logic change here in this area.
            else {
                System.out.println(nodeBInt + " -> " + nodeAInt);
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
        Graph g = new Graph(4);
        try {
            g.loadContent();
            g.printGraph();
            // path augmentation shows that there
            // exist a path between src vertex
            // to all the other vertex which shows that the graph is connected !.
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