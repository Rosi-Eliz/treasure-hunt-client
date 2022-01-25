package client.AI.PathFinder;

import java.awt.*;

public class GraphUtilities {
    public static boolean isNodeInBounds(GraphNode node, Point topLeftBound, Point bottomRightBound)
    {
        return node.getField().x >= topLeftBound.x && node.getField().x <= bottomRightBound.x
                && node.getField().y >= topLeftBound.y && node.getField().y <= bottomRightBound.y;
    }
}
