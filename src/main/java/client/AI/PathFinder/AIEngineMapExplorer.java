package client.AI.PathFinder;
import client.error_handling.MapExplorationException;
import client.game_logic.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AIEngineMapExplorer implements AIEngineExplorable{
    private MapGraph graph;

    private static Logger LOGGER = LoggerFactory.getLogger(AIEngineMapExplorer.class);

    public void setGraph(MapGraph graph) {
        this.graph = graph;
    }

    @Override
    public Queue<GameMove> getMovesFor(List<GraphNode> nodes) {
        Queue<GameMove> moves = new LinkedList<>();
        if(nodes.size() < 2)
        {
            LOGGER.error("Not enough nodes to get moves for!");
            return moves;
        }
        for(int i = 0; i < nodes.size() - 1; i++){
            final int nextIndex = i + 1;
            GraphEdge edgeToNextNode = nodes.get(i).getEdges().stream()
                    .filter(edge -> edge.getToNode()
                    .equals(nodes.get(nextIndex)))
                    .findFirst().orElse(null);
            if(edgeToNextNode != null) {
                moves.addAll(getMovesToNode(nodes.get(i), edgeToNextNode));
            } else {
                LOGGER.error("No edge to next node!");
            }
        }
        return moves;
    }

    private Queue<GameMove> getMovesToNode(GraphNode currentNode, GraphEdge edge) {
        LinkedList<GameMove> moves = new LinkedList<>();
        int xDifference = edge.getToNode().getField().x - currentNode.getField().x;
        int yDifference = edge.getToNode().getField().y - currentNode.getField().y;
        GameMove move;
        if(xDifference == 0 && yDifference == 1){
           move = GameMove.Down;
        } else if(xDifference == 1 && yDifference == 0){
            move = GameMove.Right;
        } else if(xDifference == 0 && yDifference == -1){
            move = GameMove.Up;
        } else if(xDifference == -1 && yDifference == 0){
            move = GameMove.Left;
        } else {
            LOGGER.error("Incorrect move difference!");
            throw new MapExplorationException("No moves to destination");
        }
        for(int i = 0; i < edge.getWeight(); i++){
            moves.add(move);
        }
        edge.getToNode().setIsDiscovered(true);
        return moves;
    }

    @Override
    public Queue<GameMove> exploreMap(GraphNode currentNode, Point topLeftBound, Point bottomRightBound) {
        if (graph == null) {
            LOGGER.error("Graph is not set!");
            throw new MapExplorationException("Graph is not set!");
        }
        List<GraphEdge> edges = currentNode.getEdges();

        List<GraphEdge> edgesToUnvistedNodes = edges.stream()
                .filter(edge -> !edge.getToNode().isVisited() &&
                        GraphUtilities.isNodeInBounds(edge.getToNode(), topLeftBound, bottomRightBound))
                .sorted(Comparator.comparing(GraphEdge::getWeight).reversed())
                .collect(Collectors.toList());

        if(!edgesToUnvistedNodes.isEmpty()){
            if(edgesToUnvistedNodes.stream()
                    .noneMatch(edge -> edge.getToNode().getField().terrain == GameMap.Terrain.Mountain))
            {
                Random random = new Random();
                return getMovesToNode(currentNode, edgesToUnvistedNodes.get(random.nextInt(edgesToUnvistedNodes.size())));
            }
            return getMovesToNode(currentNode, edgesToUnvistedNodes.get(0));
        } else {
            return null;
        }
    }
}
