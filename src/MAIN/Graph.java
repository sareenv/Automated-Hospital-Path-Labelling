package MAIN;


import java.util.*;


class Node {
    String label;
    ArrayList<Node> exitNodes;

    public Node(ArrayList<Node> exitNodes, String label) {
        this.exitNodes = exitNodes;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Node{" +
                "label='" + label + '\'' +
                ", exitNodes=" + exitNodes +
                '}';
    }
}


class Pair {
    String direction;
    String destinationNode;
    Node currentNode;


    public Pair(String direction,
                Node currentNode,
                String destination) {
        this.direction = direction;
        this.destinationNode = destination;
        this.currentNode = currentNode;

    }

    @Override
    public String toString() {
        return "Pair{" +
                "direction='" + direction + '\'' +
                ", destinationNode='" + destinationNode + '\'' +
                ", currentNode=" + currentNode +
                '}';
    }
}


class Graph {

    HashMap<Node, ArrayList<Node>> graph;
    Node mainEntry;
    Node mainExit;

    public void visitAllRooms(Node startNode, Node endNode) {
        Stack<Pair> stk = new Stack<>();
        ArrayList<Node> visitedNodes = new ArrayList<>();
        Node currentNode = startNode;

        while (currentNode != endNode) {


            Pair p = new Pair("l",
                    currentNode,
                    this.graph.get(currentNode).get(0).label);

            stk.add(p);
            visitedNodes.add(currentNode);
            currentNode = this.graph.get(currentNode).get(0);
        }
        visitedNodes.add(currentNode);
        Pair tempPair = new Pair("",
                currentNode,
                "");
        stk.add(tempPair);
        // print the nodes until the sink.
        for (Node node : visitedNodes) {
            System.out.println(node.label);
        }

        /*
         * Backtracking
         * Top Element;
         * */
        while (true) {
            Pair top = stk.pop();
            Pair current = stk.peek();

            Node x = null;
            Node y = null;

            ArrayList<Node> next = this.graph.get(top.currentNode);

            if (top.currentNode.exitNodes != null) {
                x = top.currentNode;
                y = top.currentNode;
                Node cx;
                Node cy;

                // reached to the main entrance again.

                if (current.currentNode.exitNodes == null) {
                    System.out.println("Reached back to the main entrance again");
                    this.graph.put(x,  new ArrayList<>
                            (Arrays.asList(current.currentNode)));
                    this.graph.put(y, new ArrayList<>(Arrays.asList(
                            current.currentNode
                    )));
                    break;
                }

                cx = current.currentNode.exitNodes.get(0);
                cy = current.currentNode.exitNodes.get(1);

                if (cx != null) {
                    next.add(cx);
                    System.out.println( "g -> " + cx.label);
                    x = cx;
                }
                if (cy != null) {
                    System.out.println("r -> " + cy.label);
                    next.add(cy);
                    y = cy;
                }
                this.graph.put(top.currentNode, next);

            } else {

                Node cx = current.currentNode.exitNodes.get(0);
                Node cy = current.currentNode.exitNodes.get(1);
                if (cx != null) {
                    next.add(cx);
                    x = cx;
                }
                if (cy != null) {
                    next.add(cy);
                    y = cy;
                }
                this.graph.put(top.currentNode, next);
            }

        }
    }



    public void buildGraph() {

        // room exit Nodes.
        Node r1Exit = new Node( null, "R1 Exit");
        Node r2Exit = new Node( null, "R2 Exit");
        Node r3Exit = new Node( null, "R3 Exit");
        Node r4Exit = new Node( null, "R4 Exit");
        Node r5Exit = new Node( null, "R5 Exit");
        Node r6Exit = new Node( null, "R6 Exit");

        Node en = new Node( null, "Exit Node");
        Node r3 = new Node(new ArrayList<>(Arrays.
                asList(r5Exit, r6Exit)), "C");
        Node r2 = new Node(new ArrayList<>(
                Arrays.asList(r3Exit, r4Exit)
        ), "B");
        Node r1 = new Node( new ArrayList<>(
                Arrays.asList(r1Exit, r2Exit)
        ), "A");

        Node entryNode = new Node(null, "Main Entrance");

        this.graph.put(entryNode, new ArrayList<>(Arrays.asList(r1)));
        this.graph.put(r1, new ArrayList<>(Arrays.asList(r2)));
        this.graph.put(r2, new ArrayList<>(Arrays.asList(r3)));
        this.graph.put(r3, new ArrayList<>(Arrays.asList(en)));
        this.graph.put(en, new ArrayList<>());

        this.mainEntry = entryNode;
        this.mainExit = en;
    }
    public Graph() {
        this.graph = new HashMap<>();
        buildGraph();
    }
}

class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.visitAllRooms(graph.mainEntry, graph.mainExit);
    }
}




