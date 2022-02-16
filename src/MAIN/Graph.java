
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

    public void loadContent(int fileNumber) throws FileNotFoundException {
        String path = "/Users/databunker/IdeaProjects/HospitalPathLabelling/src/MAIN";
        String fileName = "/Contents" + fileNumber + ".txt";
        System.out.println(fileName);
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
//        ArrayList<Object> result = printPathAugmentation();
//        ArrayList<String> cachePath = (ArrayList<String>) result.get(0);
//        if (cachePath.size() == 0) { return false; }
        return true;
    }


    public void printGraph() {
        for (int i = 0; i< this.vertices.size(); i++) {
            System.out.println( i + ": " + Arrays.toString(graph[i].toArray()));
        }
    }


    public static void pathHeuristicCost(ArrayList<Edge>[] graph,
                                         int src, HashSet<Integer> visited,
                                         ArrayList<Integer> ssp, HashMap<Integer, Integer> filledOrder) {
        // base condition
        if (ssp.size() == graph.length - 1) {

            // get the values for the filled order.
            int current_order = 1;
            filledOrder.put(0, current_order);
            for(int i = ssp.size() - 1; i != -1; i--) {
                int val = current_order;
                filledOrder.put(ssp.get(i),  val);
                current_order++;
            }
            return ;
        }

        visited.add(src);
        for (Edge e: graph[src]) {
            if (!visited.contains(e.dest)) {
                pathHeuristicCost(graph, e.dest, visited, ssp, filledOrder);
            }
        }
        if (!ssp.contains(src)) {
            ssp.add(src);
        }
        visited.remove(src);
    }

    ArrayList<Object> printPathAugmentation() {
        // paths augmentation.
        Graph g = this;
        System.out.println("Augmenting the paths from the src node 0 " +
                "to all the other paths in the graph.");
        HashSet<Integer> visited = new HashSet<>();
        HashMap<Integer, Integer> filledOrder = new HashMap<>();
        ArrayList<Integer> ssp = new ArrayList<>();
        // augment the graph in certain way.

        g.pathHeuristicCost(g.graph, 0, visited, ssp, filledOrder);
        // generate augmented paths.

        ArrayList<Object> objs = new ArrayList<>();
        // this will ensure that we label one current path correctly.
        objs.add(filledOrder);
        return objs;
    }




    // labelling Algorithm - Works based on the logic mentioned in the book.
    public static void labellingLogic(ArrayList<AugmentedPair> uniquePairs,
                                      HashMap<Integer, Integer> order) {
        ArrayList<AugmentedPair> visitedEdges = new ArrayList<>();
        HashSet<String> visitedVertices = new HashSet<>();

        for (AugmentedPair pair: uniquePairs) {
            // skip if the edge has been already marked.
            if (visitedEdges.contains(pair)) {
                continue;
            }

            int nodeAInt = Integer.parseInt(pair.nodeA + "");
            int nodeBInt = Integer.parseInt(pair.nodeB + "");

            if (!order.containsKey(nodeBInt) || !order.containsKey(nodeAInt)) {
                return;
            }

            if(order.get(nodeAInt) < order.get(nodeBInt) &&
                    !visitedVertices.contains(nodeBInt + "")) {
                System.out.println(nodeAInt + " -> " + nodeBInt);
                visitedEdges.add(pair);
                visitedVertices.add(nodeBInt + "");
                visitedVertices.add(nodeAInt + "");
            }

            else if (order.get(nodeAInt) < order.get(nodeBInt)
                    && visitedVertices.contains(nodeBInt + "")) {
                System.out.println(nodeBInt + " -> " + nodeAInt);
                visitedEdges.add(pair);
                visitedVertices.add(nodeAInt + "");
            }
        }
    }

    // String processing utility
    public static ArrayList<AugmentedPair> generateAugmentedPairs(ArrayList<String> cache) {
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
        System.out.println("Please enter the file number to load the data from ");
        int fileNumber = snc.nextInt();

        Graph g = new Graph(graphCount);
        try {
            g.loadContent(fileNumber);
            g.printGraph();
            // path augmentation shows that there
            // exist a path between src vertex
            // to all the other vertex which shows that the graph is connected !.
            if (!g.isGraphConnected()) {
                System.out.println("Sorry this graph is not connected and " +
                        "therefore cannot be labelled by algorithm for one way street problem");
                return;
            }

            int src = 0;
            HashSet<Integer> visited = new HashSet<>();
            ArrayList<Integer> spss = new ArrayList<>();
            HashMap<Integer, Integer> filledOrder = new HashMap<>();
            pathHeuristicCost(g.graph, src, visited, spss, filledOrder);
            System.out.println(filledOrder);
            // ok so we have received the filledOrder.
            // print the augmented path
            System.out.println("Please enter the number of rooms in the facility ");
            int n = snc.nextInt();
            ArrayList<String> paths = new ArrayList<>();
            System.out.println("Graph orientation");
            augmentationPaths(g.graph, src, src + "" , visited, paths, n);
            System.out.println(paths);
            ArrayList<AugmentedPair> augmentedPairList = generateAugmentedPairs(paths);
            HashSet<AugmentedPair> uniquePairs = new HashSet<>();
            for (AugmentedPair p: augmentedPairList) {
                System.out.println("node a is " + p.nodeA + " and nodeB is " + p.nodeB);
                uniquePairs.add(p);
            }
            ArrayList<AugmentedPair> uniquePair = new ArrayList<>(uniquePairs);
            labellingLogic(uniquePair, filledOrder);

        } catch (FileNotFoundException e) {
            System.out.println("File opening exception! Whoops check the " +
                    "text file containing the graph information is " +
                    "loaded properly into the program");
        } finally {
            snc.close();
        }
    }

    // print all the augmentation paths.
    private static void augmentationPaths(ArrayList<Edge>[] graph,
                  int src,
                  String psf,
                  HashSet<Integer> visited,
                  ArrayList<String> cache, int k) {

        if (visited.size() == k) {
            cache.add(psf);
            return;
        }

        // mark *
        visited.add(src);

        for (Edge e: graph[src]) {
            if (!visited.contains(e.dest)) {
                augmentationPaths(graph, e.dest, psf + e.dest, visited, cache, k);
            }
        }
        visited.remove(src);
    }
}