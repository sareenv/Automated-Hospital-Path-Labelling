
package MAIN;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.*;


class Graph {

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
    public void addEdge(int src, int destination, String type) {
        Edge edge = new Edge(src, destination, type);
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
            String type = tokens[2];
            this.addEdge(srcToken, destToken, type);
        }
        snc.close();
    }

    /*
    * Is graph connected.
    * based on the dfs utility fn:
    * */

    public void dfs(ArrayList<Edge>[] graph, int src, HashSet<Integer> visited) {
        visited.add(src);
        for (Edge e: graph[src]) {
            if (!visited.contains(e.dest)) {
                dfs(graph, e.dest, visited);
            }
        }
    }

    boolean isGraphConnected(ArrayList<Edge>[] graph,
                             HashSet<Integer> visited, int src) {
        dfs(graph, src, visited);
        return visited.size() == graph.length;
    }


    public void printGraph() {
        for (int i = 0; i< this.vertices.size(); i++) {
            System.out.println( i + ": " + Arrays.toString(graph[i].toArray()));
        }
    }

    public static void pathHeuristicCost(ArrayList<Edge>[] graph,
                                         int src,
                                         HashSet<Integer> visited,
                                         ArrayList<Integer> ssp,
                                         HashMap<Integer, Integer> filledOrder) {
        // base condition
        if (ssp.size() == graph.length - 1) {
            return;
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


    // labelling Algorithm - Works based on the logic mentioned in the book.
    public static void labellingLogic(ArrayList<AugmentedPair> uniquePairs,
                                      HashMap<Integer, Integer> order,
                                      ArrayList<String> cachedPath)  {

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
                cachedPath.add(nodeAInt + " -> " + nodeBInt);
                visitedEdges.add(pair);
                visitedVertices.add(nodeBInt + "");
                visitedVertices.add(nodeAInt + "");
            }

            else if (order.get(nodeAInt) < order.get(nodeBInt)
                    && visitedVertices.contains(nodeBInt + "")) {
                System.out.println(nodeBInt + " -> " + nodeAInt);
                cachedPath.add(nodeBInt + " -> " + nodeAInt);
                visitedEdges.add(pair);
                visitedVertices.add(nodeAInt + "");
            }
        }
    }



    // String processing utility
    public static ArrayList<AugmentedPair> generateAugmentedPairs(ArrayList<Integer> cache) {
        ArrayList<AugmentedPair> uniquePairs = new ArrayList<>();

        for (int i = 0; i < cache.size() - 1; i++) {
                int c1 = cache.get(i);
                int c2 = cache.get(i + 1);
                AugmentedPair pair = new AugmentedPair(c1, c2);
                if (!uniquePairs.contains(pair)) {
                    uniquePairs.add(pair);
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
            if (!g.isGraphConnected(g.graph, new HashSet<>(), 0)) {
                System.out.println("Sorry this graph is not connected and " +
                        "therefore cannot be labelled by algorithm for one way street problem");
                return;
            }

            int src = 0;
            HashSet<Integer> visited = new HashSet<>();
            ArrayList<Integer> spss = new ArrayList<>();
            HashMap<Integer, Integer> filledOrder = new HashMap<>();
            pathHeuristicCost(g.graph, src, visited, spss, filledOrder);

            int current_order = 1;
            filledOrder.put(0, current_order);
            for(int i = spss.size() - 1; i != -1; i--) {
                int val = current_order;
                filledOrder.put(spss.get(i),  val);
                current_order++;
            }

            // ok so we have received the filledOrder.
            // print the augmented path
            System.out.println("Please enter the number of rooms in the facility ");
            int n = snc.nextInt();
            ArrayList<String> paths = new ArrayList<>();
            System.out.println("Graph orientation");
            augmentationPaths(g.graph, src, src + "~" , visited, paths, n);

            ArrayList<AugmentedPair> augmentedPairs = new ArrayList<>();

            for (String p: paths) {
                String[] e = p.split("~");
                for (int j = 0; j< e.length; j++) {
                    String res = e[j];
                    String processed = res.replace(" ", "");
                    e[j] = processed;
                }
                System.out.println(Arrays.toString(e));
                ArrayList<Integer> nums = new ArrayList<>();

                for (String pe: e) {
                    int peint = Integer.parseInt(pe);
                    nums.add(peint);
                }
                // get pairs from here
                for (int i = 1; i< nums.size(); i++) {
                    AugmentedPair pair = new AugmentedPair(nums.get(i - 1), nums.get(i));
                    if (!augmentedPairs.contains(pair)) {
                        augmentedPairs.add(pair);
                    }
                }
            }
            ArrayList<String> cachedLabel = new ArrayList<>();
            labellingLogic(augmentedPairs, filledOrder, cachedLabel);
            labellingLogicTwoWay(g, cachedLabel);

        } catch (FileNotFoundException e) {
            System.out.println("File opening exception! Whoops check the " +
                    "text file containing the graph information is " +
                    "loaded properly into the program");
        } finally {
            snc.close();
        }
    }

    private static void labellingLogicTwoWay(Graph g, ArrayList<String> cachedLabel) {
        for (String p: cachedLabel) {
            String r1 = p.replace(" ", "");
            String r2 = r1.replace(",", "");
            String r3 = r2.replace("[", "");
            String r4 = r3.replace("]", "");
            String[] tokens = r4.split("->");
            for (ArrayList<Edge> edges: g.graph) {
                for (Edge e: edges) {
                    if (e.src == Integer.parseInt(tokens[0])
                            && e.dest == Integer.parseInt(tokens[1])) {
                        if (e.type.equals("2W")) {
                            System.out.println(tokens[1] + " -> " + tokens[0]);
                        }
                    }
                }
            }
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
                augmentationPaths(graph, e.dest, psf + e.dest + "~", visited, cache, k);
            }
        }
        visited.remove(src);
    }
}