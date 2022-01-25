package client.AI.PathFinder;

import java.util.Objects;

public class GraphEdge {
    Integer weight;
    GraphNode toNode;

    public GraphEdge(Integer weight, GraphNode toNode) {
        this.weight = weight;
        this.toNode = toNode;
    }

    public Integer getWeight() {
        return weight;
    }

    public GraphNode getToNode() {
        return toNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphEdge graphEdge = (GraphEdge) o;
        return Objects.equals(weight, graphEdge.weight) && Objects.equals(toNode, graphEdge.toNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, toNode.getField());
    }
}
