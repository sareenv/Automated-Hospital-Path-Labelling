package MAIN;

class Edge {
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
