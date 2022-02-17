package MAIN;

class Edge {
    int src;
    int dest;
    String type;

    public Edge(int src, int dest) {
        this.src = src;
        this.dest = dest;
    }

    public Edge(int src, int dest, String type) {
        this.src = src;
        this.dest = dest;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "src=" + src +
                ", dest=" + dest +
                ", type='" + type + '\'' +
                '}';
    }
}
