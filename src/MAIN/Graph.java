package MAIN;


import javafx.scene.effect.Effect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


//class Node {
//    String label;
//    ArrayList<Node> exitNodes;
//
//    public Node(ArrayList<Node> exitNodes, String label) {
//        this.exitNodes = exitNodes;
//        this.label = label;
//    }
//
//    @Override
//    public String toString() {
//        return "Node{" +
//                "label='" + label + '\'' +
//                ", exitNodes=" + exitNodes +
//                '}';
//    }
//}
//
//
//class Pair {
//    String direction;
//    String destinationNode;
//    Node currentNode;
//
//
//    public Pair(String direction,
//                Node currentNode,
//                String destination) {
//        this.direction = direction;
//        this.destinationNode = destination;
//        this.currentNode = currentNode;
//
//    }
//
//    @Override
//    public String toString() {
//        return "Pair{" +
//                "direction='" + direction + '\'' +
//                ", destinationNode='" + destinationNode + '\'' +
//                ", currentNode=" + currentNode +
//                '}';
//    }
//}
//
//
//class Graph {
//
//    HashMap<Node, ArrayList<Node>> graph;
//    Node mainEntry;
//    Node mainExit;
//
//    public void visitAllRooms(Node startNode, Node endNode) {
//        Stack<Pair> stk = new Stack<>();
//        ArrayList<Node> visitedNodes = new ArrayList<>();
//        StringBuilder sinkPath = new StringBuilder();
//        Node currentNode = startNode;
//
//
//        while (currentNode != endNode) {
//            Pair p = new Pair("left",
//                    currentNode,
//                    this.graph.get(currentNode).get(0).label);
//
//            stk.add(p);
//            visitedNodes.add(currentNode);
//            sinkPath.append("left\n");
//            currentNode = this.graph.get(currentNode).get(0);
//        }
//        visitedNodes.add(currentNode);
//        Pair tempPair = new Pair("",
//                currentNode,
//                "");
//        stk.add(tempPair);
//
//        StringBuilder exitPath1 = new StringBuilder();
//        StringBuilder exitPath2 = new StringBuilder();
//
//
//        /*
//         * Backtracking
//         * Top Element;
//         * */
//        while (true) {
//            Pair top = stk.pop();
//            Pair current = stk.peek();
//
//            Node x = null;
//            Node y = null;
//
//            ArrayList<Node> next = this.graph.get(top.currentNode);
//
//            if (top.currentNode.exitNodes != null) {
//                x = top.currentNode;
//                y = top.currentNode;
//                Node cx;
//                Node cy;
//
//                // reached to the main entrance again.
//                if (current.currentNode.exitNodes == null) {
//
//                    this.graph.put(x,  new ArrayList<>
//                            (Arrays.asList(current.currentNode)));
//                    this.graph.put(y, new ArrayList<>(Arrays.asList(
//                            current.currentNode
//                    )));
//                    exitPath1.append("down\nright\n");
//                    exitPath2.append("up\nright\n");
//                    break;
//                }
//
//                cx = current.currentNode.exitNodes.get(0);
//                cy = current.currentNode.exitNodes.get(1);
//
//                if (cx != null) {
//                    next.add(cx);
//                    exitPath1.append("right\n");
//                    x = cx;
//                }
//
//                if (cy != null) {
//                    next.add(cy);
//                    exitPath2.append("right\n");
//                    y = cy;
//                }
//                this.graph.put(top.currentNode, next);
//
//            } else {
//                Node cx = current.currentNode.exitNodes.get(0);
//                Node cy = current.currentNode.exitNodes.get(1);
//                if (cx != null) {
//
//                    next.add(cx);
//                    exitPath1.append("up\nright\n");
//                    x = cx;
//                }
//                if (cy != null) {
//                    next.add(cy);
//                    exitPath2.append("down\nright\n");
//                    y = cy;
//                }
//                this.graph.put(top.currentNode, next);
//            }
//        }
//        System.out.println("Labelled first exit path as " +
//                "is \n" + sinkPath.toString() + exitPath1.toString());
//
//        System.out.println("Labelled second exit path as " +
//                "is \n" + sinkPath.toString() + exitPath2.toString());
//    }
//
//
//
//    public void buildGraph() {
//
//        // room exit Nodes.
//        Node r1Exit = new Node( null, "R1 Exit");
//        Node r2Exit = new Node( null, "R2 Exit");
//        Node r3Exit = new Node( null, "R3 Exit");
//        Node r4Exit = new Node( null, "R4 Exit");
//        Node r5Exit = new Node( null, "R5 Exit");
//        Node r6Exit = new Node( null, "R6 Exit");
//
//        Node en = new Node( null, "Exit Node");
//        Node r3 = new Node(new ArrayList<>(Arrays.
//                asList(r5Exit, r6Exit)), "C");
//        Node r2 = new Node(new ArrayList<>(
//                Arrays.asList(r3Exit, r4Exit)
//        ), "B");
//        Node r1 = new Node( new ArrayList<>(
//                Arrays.asList(r1Exit, r2Exit)
//        ), "A");
//
//        Node entryNode = new Node(null, "Main Entrance");
//
//        this.graph.put(entryNode, new ArrayList<>(Arrays.asList(r1)));
//        this.graph.put(r1, new ArrayList<>(Arrays.asList(r2)));
//        this.graph.put(r2, new ArrayList<>(Arrays.asList(r3)));
//        this.graph.put(r3, new ArrayList<>(Arrays.asList(en)));
//        this.graph.put(en, new ArrayList<>());
//
//        this.mainEntry = entryNode;
//        this.mainExit = en;
//    }
//    public Graph() {
//        this.graph = new HashMap<>();
//        buildGraph();
//    }
//}

class Main {

    static class Node {
        String name;
        ArrayList<String> rooms;
        public Node(String name, ArrayList<String> rooms) {
            this.name = name;
            this.rooms = rooms;
        }

        @Override
        public String toString() {
            return "Node \n" +
                    "name='" + name + '\'' +
                    ", rooms=" + rooms +
                    '}' + '\n';
        }
    }

    enum Directions {East, West, North, South};

    static class Edge {
        Node src;
        Node destinations;
        Directions direction;

        public Edge(Node src, Node destinations, String direction) {
            this.src = src;
            this.destinations = destinations;
            switch (direction) {
                case "E":
                    this.direction = Directions.East;
                    break;
                case "W":
                    this.direction = Directions.West;
                    break;
                case "N":
                    this.direction = Directions.North;
                    break;
                default:
                    this.direction = Directions.South;
                    break;
            }
        }

        @Override
        public String toString() {
            return "Edge{\n" +
                    "src=" + src +
                    ", destinations=" + destinations +
                    ", direction=" + direction +
                    '}';
        }
    }



        public static void hamiltonionPath(HashMap<Node, ArrayList<Edge>> graph, Node src,
                                           HashSet<Node> visited,
                                       String psf, ArrayList<String> cache) {
        if (visited.size() == graph.size() - 1) {
            cache.add(psf);
            return;
        }
        visited.add(src);

        ArrayList<Edge> edges = graph.get(src);

        if (edges == null) {
            System.out.println("Edge is null");
            return;
        }

        for (Edge e: edges) {

            if (!visited.contains(e.destinations)) {
                hamiltonionPath(graph, e.destinations, visited,
                        psf + e.destinations, cache);
            }
        }
        visited.remove(src);
    }


    public static void main(String[] args) {
        HashMap<Node, ArrayList<Edge>> graph = new HashMap<>();
        HashSet<Node> nodes = new HashSet<>();
        ArrayList<Edge> edges = new ArrayList<>();
        Scanner snc = null;
        try {
            String basePath = "/Users/databunker/IdeaProjects/HospitalPathLabelling/src/MAIN/";
            String relativePath = "Contents.txt";
            String finalPath = basePath + relativePath;
            File file = new File(finalPath);
            snc = new Scanner(file);

            while (snc.hasNext()) {
                String line = snc.nextLine();
                String[] vals = line.split(",");
                Node v1 = new Node(vals[0], new ArrayList<>());
                Node v2 = new Node(vals[1], new ArrayList<>());

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (snc != null) {
                snc.close();
            }
        }



//        Node srcNode = (Node) graph.keySet().toArray()[0];
//        HashSet<Node> visited = new HashSet<>();
//        System.out.println();
//        hamiltonionPath(graph, srcNode, visited, srcNode.name + "", new ArrayList<>());

        for (Node n: nodes) {
            System.out.println("Node is " + n.name);
        }

    }
}




