package MAIN;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;


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




class Graph {

    HashMap<Node, ArrayList<Node>> graph;
    Node mainEntry;
    Node mainExit;

    public void visitAllRooms(Node startNode, Node endNode) {
        HashMap<Node, Boolean> visitedNodeStatus = new HashMap<>();
        Stack<Node> stk = new Stack<>();
        ArrayList<Node> visitedNodes = new ArrayList<>();
        Node currentNode = startNode;
        StringBuilder psf = new StringBuilder();

        while (currentNode != endNode) {
            stk.add(currentNode);
            visitedNodeStatus.put(currentNode, true);
            psf
                .append(currentNode.label + " -> ")
                .append(this.graph.get(currentNode).get(0).label + " using left direction \n");
            visitedNodes.add(currentNode);
            currentNode = this.graph.get(currentNode).get(0);
        }
        visitedNodes.add(currentNode);
        System.out.println(psf.toString());

        // backtracking started.
        Node lastNode = stk.pop();
        Node topNode = stk.peek();
        ArrayList<Node> exitNodes = topNode.exitNodes;

        for(Node exitNode: exitNodes) {
            ArrayList lst = this.graph.get(topNode);
            lst.add(exitNode);
            System.out.println("the list is " + lst);
            this.graph.put(lastNode, lst);
        }
        System.out.println(this.graph.get(exitNodes));
        System.out.println(this.mainExit);
    }



    public void buildGraph() {

        // room exit Nodes.
        Node r1Exit = new Node( null, "R1 Exist");
        Node r2Exit = new Node( null, "R2 Exist");
        Node r3Exit = new Node( null, "R3 Exist");
        Node r4Exit = new Node( null, "R4 Exist");
        Node r5Exit = new Node( null, "R5 Exist");
        Node r6Exit = new Node( null, "R6 Exist");

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




