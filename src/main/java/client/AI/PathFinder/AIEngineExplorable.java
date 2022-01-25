package client.AI.PathFinder;

import java.awt.*;
import java.util.List;
import java.util.Queue;

public interface AIEngineExplorable {
    Queue<GameMove> exploreMap(GraphNode currentNode, Point topLeftBound, Point bottomRightBound);
    Queue<GameMove> getMovesFor(List<GraphNode> nodes);
    void setGraph(MapGraph mapGraph);
}
