package client.AI.PathFinder;
import client.game_logic.GameMap;
import org.junit.jupiter.api.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class GraphUtilitiesTests {
    private MapGraph graph;
    private GameMap map;

    @BeforeEach
    public void setUp() {
        map = new GameMap(8, 4);
        graph = new MapGraph(map);
    }

    @Test
    @DisplayName("Test is node in bounds")
    public void testIsNodeInBounds() {
        GraphUtilities graphUtilities = new GraphUtilities();
        assertTrue(graphUtilities.isNodeInBounds(graph.getNode(map.getFieldForCoordinates(0, 0)), new Point(0,0), new Point(7,3)));
        assertTrue(graphUtilities.isNodeInBounds(graph.getNode(map.getFieldForCoordinates(2, 2)), new Point(0,0), new Point(2,2)));
        assertTrue(graphUtilities.isNodeInBounds(graph.getNode(map.getFieldForCoordinates(1, 1)), new Point(0,0), new Point(2,2)));
    }

    @Test
    @DisplayName("Test is node not in bounds")
    public void testIsNodeNotInBounds() {
        GraphUtilities graphUtilities = new GraphUtilities();
        assertFalse(graphUtilities.isNodeInBounds(graph.getNode(map.getFieldForCoordinates(0, 0)), new Point(1,1), new Point(7,3)));
        assertFalse(graphUtilities.isNodeInBounds(graph.getNode(map.getFieldForCoordinates(2, 3)), new Point(0,0), new Point(2,2)));
        assertFalse(graphUtilities.isNodeInBounds(graph.getNode(map.getFieldForCoordinates(3, 3)), new Point(0,0), new Point(2,2)));
    }
}
