package MAIN;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Gr {

    static class Pair implements Comparable<Pair> {
        int vertex;
        String path;
        int cost;

        public Pair(int vertex ,String path, int cost) {
            this.vertex = vertex;
            this.path = path;
            this.cost = cost;
        }

        @Override
        public int compareTo(Pair o) {
            return this.cost - o.cost;
        }
    }

    static int V = 9;

    static class Edge {
        int src;
        int destination;
        int cost;

        public Edge(int src, int vertex, int cost) {
            this.src = src;
            this.destination = vertex;
            this.cost = cost;
        }
    }


    public static ArrayList<Edge>[] buildGraph() {
        int edgeCnt = 7;
        ArrayList<Edge> graphs[] = new ArrayList[edgeCnt];

        for (int i = 0; i< edgeCnt; i++) {
            graphs[i] = new ArrayList<>();
        }

        graphs[0].add(new Edge(0, 1, 10));
        graphs[0].add(new Edge(0, 3, 40));

        graphs[1].add(new Edge(1, 0, 10));
        graphs[1].add(new Edge(1, 2, 10));

        graphs[2].add(new Edge(2, 3, 10));
        graphs[2].add(new Edge(2, 1, 10));

        graphs[3].add(new Edge(3, 0, 40));
        graphs[3].add(new Edge(3, 4, 2));
        graphs[3].add(new Edge(3, 2, 10));

        graphs[4].add(new Edge(4, 5, 3));
        graphs[4].add(new Edge(4, 6, 8));

        graphs[5].add(new Edge(5, 4, 3));
        graphs[5].add(new Edge(5, 6, 3));

        graphs[6].add(new Edge(6, 4, 8));
        graphs[6].add(new Edge(6, 5, 3));
        return graphs;
    }

    // bfs algorithm from src to the destination.
    public static void bfsAlgorithm(ArrayList<Edge>[] graph,  int src,
                                    int destination, boolean[] visited) {
        // Queue is the interface and not the actual class in the java,
        // so we use the array deque class

        Queue<Pair> queue = new ArrayDeque<>();
        // add the src to the queue.

        queue.add(new Pair(src, src + "", 0));

        while (!queue.isEmpty()) {
            // rmwa.
            Pair p = queue.remove();
            if (p.vertex == destination) {
                System.out.println("Path is from " + src +
                        " to destination " +  destination + " is " + p.path
                        + " with cost of " + p.cost);
                return;
            }

            if (!visited[p.vertex]) {
                visited[p.vertex] = true;
                for (Edge e: graph[p.vertex]) {
                    if (!visited[e.destination]) {
                        queue.add(new Pair(e.destination, p.path + e.destination
                                + "", p.cost + e.cost));
                    }
                }
            }
        }
        System.out.println("No path exist");
    }

    // find the shortest path from the src vertex to the every other vertex
    // with the priority queue simple and easy to follow.
    // remove mark work add.
    public static void dijkstrasAlgorithm(ArrayList<Edge>[] graph, int src, boolean[] visited) {
        PriorityQueue<Pair> queue = new PriorityQueue<>();
        queue.add(new Pair(src, src + "", 0));

        while (!queue.isEmpty()) {
            Pair p = queue.poll();

            if (!visited[p.vertex]) {
                visited[p.vertex] = true;
                System.out.println("from src 0 -> " + p.vertex +
                        " with the cost of " + p.cost + " via path " + p.path);
                for (Edge e: graph[p.vertex]) {
                    if (!visited[e.destination]) {
                        queue.add(new Pair(e.destination, p.path + e.destination,
                                p.cost + e.cost));

                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        ArrayList<Edge>[] graph = buildGraph();
        boolean[] visited = new boolean[graph.length];
        bfsAlgorithm(graph, 0, 3, visited);
        dijkstrasAlgorithm(graph, 0, visited);

    }

}





