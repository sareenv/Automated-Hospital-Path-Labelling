package MAIN;


import java.util.Objects;

class AugmentedPair {
    Integer nodeA;
    Integer nodeB;


    public AugmentedPair(Integer nodeA, Integer nodeB) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }


    @Override
    public String toString() {
        return "AugmentedPair{" +
                "nodeA=" + nodeA +
                ", nodeB=" + nodeB +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AugmentedPair pair = (AugmentedPair) o;
        return nodeA == pair.nodeA && nodeB == pair.nodeB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeA, nodeB);
    }
}