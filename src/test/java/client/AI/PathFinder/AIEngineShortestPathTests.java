package client.AI.PathFinder;

import client.AI.PathFinder.AIEngineShortestPath;
import client.AI.PathFinder.MapGraph;
import client.game_logic.GameMap;
import org.junit.jupiter.api.*;

public class AIEngineShortestPathTests {
    private MapGraph graph;
    private GameMap map;
    private AIEngineShortestPath aiEngine;

    @BeforeEach
    public void setup() {
        map = new GameMap(10, 10);
        map.getAllFields().stream().forEach(field -> field.terrain = GameMap.Terrain.Grass);
        map.getFieldForCoordinates(1, 0).terrain = GameMap.Terrain.Mountain;
        map.getFieldForCoordinates(2, 0).terrain = GameMap.Terrain.Mountain;
        map.getFieldForCoordinates(3, 0).terrain = GameMap.Terrain.Mountain;
        graph = new MapGraph(map);
        aiEngine = new AIEngineShortestPath();
    }

    @Test
    @DisplayName("Test shortest path")
    public void testShortestPath() {
        var shortestPath = aiEngine.calculateShortestPathBetween(graph.getNode(map.getFieldForCoordinates(0, 0)),
                graph.getNode(map.getFieldForCoordinates(4, 0)), graph);
        Assertions.assertEquals(shortestPath.size(), 7);
        Assertions.assertAll(
                () -> Assertions.assertEquals(shortestPath.get(0).getField(), map.getFieldForCoordinates(0, 0)),
                () -> Assertions.assertEquals(shortestPath.get(1).getField(), map.getFieldForCoordinates(0, 1)),
                () -> Assertions.assertEquals(shortestPath.get(2).getField(), map.getFieldForCoordinates(1, 1)),
                () -> Assertions.assertEquals(shortestPath.get(3).getField(), map.getFieldForCoordinates(2, 1)),
                () -> Assertions.assertEquals(shortestPath.get(4).getField(), map.getFieldForCoordinates(3, 1)),
                () -> Assertions.assertEquals(shortestPath.get(5).getField(), map.getFieldForCoordinates(4, 1)),
                () -> Assertions.assertEquals(shortestPath.get(6).getField(), map.getFieldForCoordinates(4, 0))
        );
    }

    @Test
    @DisplayName("Test longest shortest path")
    void testLongestShortestPath() {
        var shortestPath = aiEngine.calculateShortestPathBetween(graph.getNode(map.getFieldForCoordinates(0, 0)),
                graph.getNode(map.getFieldForCoordinates(9, 9)), graph);
        Assertions.assertEquals(shortestPath.size(), 19);
    }
}
