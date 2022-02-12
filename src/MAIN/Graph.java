
package MAIN;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

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

    public void addVertex(int v) {
        vertices.add(v);
    }

    // creates the edges between the src and dest and adds to the graph G;
    public void addEdge(int src, int destination) {
        Edge edge = new Edge(src, destination);
        this.graph[src].add(edge);
    }

    public void loadContent() throws FileNotFoundException {
        String path = "/Users/databunker/IdeaProjects/HospitalPathLabelling/src/MAIN";
        String fileName = "/Contents.txt";
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

    void printPathAugmentation() {
        // paths augmentation.
        Graph g = this;
        System.out.println("Augmenting the paths from the src node 0 " +
                "to all the other paths in the graph.");
        HashSet<Integer> visited = new HashSet<>();
        ArrayList<String> cache = new ArrayList<>();
        int[] order = new int[g.vertices.size()];
        g.pathsAugmentation(g.graph, 0, visited,  0 + "", cache , order, 0);
        for (String p: cache) {
            System.out.println(p);
        }
    }

    public static void main(String[] args) {
        Graph g = new Graph(4);
        try {
            g.loadContent();
            g.printGraph();
            g.printPathAugmentation();
            // next step is to perform the one way labelling to the graph.

        } catch (FileNotFoundException e) {
            System.out.println("File opening exception! Whoops check the " +
                    "text file containing the graph information is " +
                    "loaded properly into the system");
        }
    }
}