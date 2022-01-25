package client.AI.PathFinder;

import client.game_logic.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.List;

public class AIEngineMapExplorerTests {
    private AIEngineMapExplorer aiEngineMapExplorer;
    private GameMap map;
    private MapGraph graph;

    @BeforeEach
    public void setup() {
        map = new GameMap(10, 10);
        map.getAllFields().stream().forEach(field -> field.terrain = GameMap.Terrain.Grass);
        map.getFieldForCoordinates(2, 1).terrain = GameMap.Terrain.Mountain;
        map.getFieldForCoordinates(2, 2).terrain = GameMap.Terrain.Mountain;
        map.getFieldForCoordinates(1, 2).terrain = GameMap.Terrain.Water;
        map.getFieldForCoordinates(1, 4).terrain = GameMap.Terrain.Water;
        map.getFieldForCoordinates(2, 3).terrain = GameMap.Terrain.Mountain;
        map.getFieldForCoordinates(0, 8).terrain = GameMap.Terrain.Mountain;

        graph = new MapGraph(map);
        graph.getNode(map.getFieldForCoordinates(2, 3)).setIsDiscovered(true);
        graph.getNode(map.getFieldForCoordinates(0, 8)).setIsDiscovered(true);
        graph.getNode(map.getFieldForCoordinates(1, 9)).setIsDiscovered(true);

        aiEngineMapExplorer = new AIEngineMapExplorer();
        aiEngineMapExplorer.setGraph(graph);
    }

    @Test
    @DisplayName("Test get Moves for nodes")
    public void testGetMoves() {
        List<GraphNode> nodes = List.of(graph.getNode(map.getFieldForCoordinates(0, 0)),
                                        graph.getNode(map.getFieldForCoordinates(1, 0)),
                                        graph.getNode(map.getFieldForCoordinates(2, 0)),
                                        graph.getNode(map.getFieldForCoordinates(2, 1)),
                                        graph.getNode(map.getFieldForCoordinates(2, 2)),
                                        graph.getNode(map.getFieldForCoordinates(2, 3)),
                                        graph.getNode(map.getFieldForCoordinates(3, 3)));

        var moves = aiEngineMapExplorer.getMovesFor(nodes);
        Assertions.assertEquals(moves.size(), 18);

        Assertions.assertAll(
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Down),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right));
    }

    @Test
    @DisplayName("Test explore map with discovered neighbours")
    public void testExploreMap() {
        var moves = aiEngineMapExplorer.exploreMap(graph.getNode(map.getFieldForCoordinates(0, 9)),
                                                   new Point(0,0),
                                                   new Point(9,9));
        Assertions.assertNull(moves);
    }

    @Test
    @DisplayName("Test explore map with discovered neighbours")
    public void testExploreMap2() {
        var moves = aiEngineMapExplorer.exploreMap(graph.getNode(map.getFieldForCoordinates(1, 1)),
                                                   new Point(0,0),
                                                   new Point(9,9));
        Assertions.assertNotNull(moves);
        Assertions.assertEquals(moves.size(), 3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Right));
    }

    @Test
    @DisplayName("Test explore map with discovered neighbours")
    public void testExploreMap3() {
        var moves = aiEngineMapExplorer.exploreMap(graph.getNode(map.getFieldForCoordinates(1, 3)),
                                                   new Point(0,0),
                                                   new Point(9,9));
        Assertions.assertNotNull(moves);
        Assertions.assertEquals(moves.size(), 2);
        Assertions.assertAll(
                () -> Assertions.assertEquals(moves.poll(), GameMove.Left),
                () -> Assertions.assertEquals(moves.poll(), GameMove.Left));
    }

    @Test
    @DisplayName("Test explore map with discovered neighbours and out of bounds")
    public void testExploreMap4() {
        var moves = aiEngineMapExplorer.exploreMap(graph.getNode(map.getFieldForCoordinates(1, 1)),
                new Point(2,2),
                new Point(9,9));
        Assertions.assertNull(moves);
    }

    @Test
    @DisplayName("Test explore map with discovered neighbours and out of bounds")
    public void testExploreMap5() {
        var moves = aiEngineMapExplorer.exploreMap(graph.getNode(map.getFieldForCoordinates(1, 3)),
                new Point(3,4),
                new Point(9, 9));
        Assertions.assertNull(moves);
    }
}
