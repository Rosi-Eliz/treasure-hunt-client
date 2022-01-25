package client.AI.PathFinder;

import java.util.List;
import java.util.Queue;

public interface AIEngineShortestPathFindable {
    List<GraphNode> calculateShortestPathBetween(GraphNode start, GraphNode end, MapGraph graph);
}
