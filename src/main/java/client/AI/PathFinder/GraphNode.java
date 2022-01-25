package client.AI.PathFinder;

import client.game_logic.GameMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class GraphNode {
    private GameMap.GameField field;
    private boolean isDiscovered;
    private List<GraphEdge> edges;

    public GraphNode(GameMap.GameField field, boolean isDiscovered, List<GraphEdge> edges) {
        this.field = field;
        this.isDiscovered = isDiscovered;
        this.edges = edges;
    }

    public GameMap.GameField getField() {
        return field;
    }

    public boolean isVisited() {
        return isDiscovered;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public void setIsDiscovered(boolean isDiscovered) {
        this.isDiscovered = isDiscovered;
    }


    public void setEdge(GraphEdge edge) {
        if(edges == null) {
            edges = new LinkedList<GraphEdge>();
        }
        edges.add(edge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNode graphNode = (GraphNode) o;
        return Objects.equals(field, graphNode.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
