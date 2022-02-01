package MAIN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;


class Node {
    String label;
    Edge next;
    ArrayList<Node> exitNodes;

    public Node(Edge next, ArrayList<Node> exitNodes, String label) {
        this.next = next;
        this.exitNodes = exitNodes;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Node{" +
                "label='" + label + '\'' +
                ", next=" + next +
                ", exitNodes=" + exitNodes +
                '}';
    }
}


class Edge {

    enum EdgeDirection {Left, Right};

    Node dest;
    public Edge(Node dest) {
        this.dest = dest;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "dest=" + dest +
                '}';
    }
}

class Graph {

    HashMap<Node, ArrayList<Node>> graph;
    private Node mainEntry;
    private Node mainExit;

    public static void visitAllRooms(Node startNode, Node endNode) {
        HashMap<Node, Boolean> visitedNodeStatus = new HashMap<>();
        Stack<Node> stk = new Stack<>();
        ArrayList<Node> visitedNodes = new ArrayList<>();
        Node currentNode = startNode;
        StringBuilder psf = new StringBuilder();

        while (currentNode != endNode) {
            stk.add(currentNode);
            visitedNodeStatus.put(currentNode, true);
            psf.append(currentNode.label)
                    .append(": left -> ")
                    .append(currentNode.next.dest.label + " \n");
            visitedNodes.add(currentNode);
            currentNode = currentNode.next.dest;
        }
        visitedNodes.add(currentNode);
        System.out.println(psf.toString());


        // backtracking started.
        Node lastNode = stk.pop();
        /*
            lastNode = exit node
            [
                C,
                B,
                A,
                Entrance
            ]
         */
        Node topNode = stk.peek();
        ArrayList<Node> exitNodes = topNode.exitNodes;


    }



    public void buildGraph() {

        // room exit Nodes.
        Node r1Exit = new Node(null, null, "R1 Exist");
        Node r2Exit = new Node(null, null, "R2 Exist");
        Node r3Exit = new Node(null, null, "R3 Exist");
        Node r4Exit = new Node(null, null, "R4 Exist");
        Node r5Exit = new Node(null, null, "R5 Exist");
        Node r6Exit = new Node(null, null, "R6 Exist");


        Node en = new Node(null, null, "Exit Node");
        Node r3 = new Node(new Edge(en), new ArrayList<>(Arrays.
                asList(r5Exit, r6Exit)), "C");
        Node r2 = new Node(new Edge(r3), new ArrayList<>(
                Arrays.asList(r3Exit, r4Exit)
        ), "B");
        Node r1 = new Node(new Edge(r2), new ArrayList<>(
                Arrays.asList(r1Exit, r2Exit)
        ), "A");
        Node node = new Node(new Edge(r1), null, "Main Entrance");
        this.mainEntry = node;
        this.mainExit = en;
    }
    public Graph() {
        this.graph = new HashMap<>();
        buildGraph();
        visitAllRooms(this.mainEntry, mainExit);
    }
}

class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();

    }
}




