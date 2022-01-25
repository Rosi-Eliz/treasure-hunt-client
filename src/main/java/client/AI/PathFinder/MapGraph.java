package client.AI.PathFinder;

import client.error_handling.GraphShortestPathException;
import client.game_logic.GameMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MapGraph {
    private GameMap map;
    private HashMap<String, GraphNode> graph = new HashMap<>();

    public MapGraph(GameMap map) {
        this.map = map;
        constructGraph();
    }

    private void constructGraph()
    {
        initializeNodes();
        for(GraphNode node : graph.values())
        {
            getNeighbours(node).forEach(neighbour -> {
                int weight = calculateWeightBetween(node.getField(), neighbour.getField());
                node.setEdge(new GraphEdge(weight, neighbour));
            });
        }
    }

    private String constructKeyForField(GameMap.GameField field)
    {
        return field.x + "-" + field.y;
    }

    private void initializeNodes()
    {
        for(GameMap.GameField field : map.getALlFieldsExcludingWaters())
        {
            GraphNode node = new GraphNode(field, false, new LinkedList<>());
            graph.put(constructKeyForField(field), node);
        }
    }

    private List<GraphNode> getNeighbours(GraphNode node)
    {
        return map.getAllNeighboursExceptWater(node.getField()).stream()
                .map(field -> graph.get(constructKeyForField(field))).collect(Collectors.toList());
    }

    public GraphNode getNode(GameMap.GameField field)
    {
        return graph.get(constructKeyForField(field));
    }

    private int calculateWeightBetween(GameMap.GameField field1, GameMap.GameField field2) {
       int result = 0;
       List<GameMap.GameField> fields = List.of(field1, field2);
       for(GameMap.GameField field : fields) {
           switch (field.terrain) {
               case Grass:
                   result += GameMap.grassSteps;
                   break;
               case Mountain:
                   result += GameMap.mountainSteps;
                   break;
               case Water:
                   throw new GraphShortestPathException("Water is not allowed");
           }
       }
       return result;
    }

    public List<GraphNode> getAllNodes()
    {
        return graph.values().stream().collect(Collectors.toList());
    }

    public List<GraphNode> getUnvisitedNodes()
    {
        return graph.values().stream().filter(node -> !node.isVisited()).collect(Collectors.toList());
    }

}
