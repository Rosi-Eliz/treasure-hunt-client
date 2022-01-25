package client.AI.PathFinder;

import client.game_logic.GameMap;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.awt.*;
import java.util.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

public class AIEnginePathFinderTests {
    private AIEngineShortestPathFindable shortestPathFinder;
    private AIEngineExplorable mapExplorer;
    private AIEnginePathFinder pathFinder;
    private GameMap map;
    private MapGraph graph;

    @BeforeEach
    public void setup() {
        shortestPathFinder = Mockito.mock(AIEngineShortestPathFindable.class);
        mapExplorer = Mockito.mock(AIEngineExplorable.class);

        map = new GameMap(8,8);
        map.getAllFields().stream().forEach(field -> field.terrain = GameMap.Terrain.Grass);
        map.getFieldForCoordinates(0, 0).terrain = GameMap.Terrain.Mountain;
        map.getFieldForCoordinates(1, 0).terrain = GameMap.Terrain.Water;

        map.getFieldForCoordinates(0, 0).playerPositionState = GameMap.PlayerPositionState.MyPlayerPosition;
        pathFinder = new AIEnginePathFinder(shortestPathFinder, mapExplorer);
        pathFinder.setMap(map);
        graph = new MapGraph(map);
    }

    @Test
    @DisplayName("Test calculateNextMoveFor with no treasure or enemy castle discovered")
    public void testCalculateNextMove() {
        Mockito.when(mapExplorer.exploreMap(Mockito.any(GraphNode.class), Mockito.any(Point.class), Mockito.any(Point.class)))
               .thenReturn(new LinkedList<>(Arrays.asList(GameMove.Left, GameMove.Left)));
        assertEquals(pathFinder.calculateNextMoveFor(map.getFieldForCoordinates(0,0)), GameMove.Left);
        assertEquals(pathFinder.calculateNextMoveFor(map.getFieldForCoordinates(0,0)), GameMove.Left);
    }

    @Test
    @DisplayName("Test calculateNextMoveFor with no unvisited neighbours")
    public void testCalculateNextMove2() {
        Mockito.when(mapExplorer.exploreMap(Mockito.any(GraphNode.class), Mockito.any(Point.class), Mockito.any(Point.class)))
               .thenReturn(null);

        Mockito.when(shortestPathFinder.calculateShortestPathBetween(Mockito.any(GraphNode.class), Mockito.any(GraphNode.class), Mockito.any(MapGraph.class)))
                        .thenReturn(List.of(graph.getNode(map.getFieldForCoordinates(0,0))));

        Mockito.when(mapExplorer.getMovesFor(Mockito.anyList()))
                .thenReturn(new LinkedList<>(Arrays.asList(GameMove.Down)));

        assertEquals(pathFinder.calculateNextMoveFor(map.getFieldForCoordinates(0,0)), GameMove.Down);

//        Mockito.verify(shortestPathFinder).calculateShortestPathBetween(
//                argThat((GraphNode graphNode) ->
//                        graphNode.getField().x.equals(0) && graphNode.getField().y.equals(0)),
//                argThat((GraphNode graphNode) ->
//                        graphNode.getField().x.equals(0) && graphNode.getField().y.equals(2)),
//                argThat((MapGraph graph) ->
//                        true));
    }

    @Test
    @DisplayName("Test calculateNextMoveFor with no unvisited neighbours for incorrect path")
    public void testCalculateNextMove3() {
        Mockito.when(mapExplorer.exploreMap(Mockito.any(GraphNode.class), Mockito.any(Point.class), Mockito.any(Point.class)))
                .thenReturn(null);

        Mockito.when(shortestPathFinder.calculateShortestPathBetween(Mockito.any(GraphNode.class), Mockito.any(GraphNode.class), Mockito.any(MapGraph.class)))
                .thenReturn(List.of(graph.getNode(map.getFieldForCoordinates(0,1))));

        Mockito.when(mapExplorer.getMovesFor(Mockito.anyList()))
                .thenReturn(new LinkedList<>(Arrays.asList(GameMove.Down)));

        assertEquals(pathFinder.calculateNextMoveFor(map.getFieldForCoordinates(0,0)), null);

    }

    @Test
    @DisplayName("Test calculateNextMoveFor with treasure node")
    public void testCalculateNextMove4() {
        map.getFieldForCoordinates(3,3).gameTreasureState = GameMap.GameTreasureState.MyTreasureIsPresent;
        pathFinder.setMap(map);

        Mockito.when(shortestPathFinder.calculateShortestPathBetween(Mockito.any(GraphNode.class), Mockito.any(GraphNode.class), Mockito.any(MapGraph.class)))
                .thenReturn(List.of(graph.getNode(map.getFieldForCoordinates(0,0))));

        Mockito.when(mapExplorer.getMovesFor(Mockito.anyList()))
                .thenReturn(new LinkedList<>(Arrays.asList(GameMove.Left)));

        assertEquals(pathFinder.calculateNextMoveFor(map.getFieldForCoordinates(0,0)), GameMove.Left);

        Mockito.verify(shortestPathFinder).calculateShortestPathBetween(
                argThat((GraphNode graphNode) ->
                        graphNode.getField().x.equals(0) && graphNode.getField().y.equals(0)),
                argThat((GraphNode graphNode) ->
                        graphNode.getField().x.equals(3) && graphNode.getField().y.equals(3)),
                argThat((MapGraph graph) ->
                        true));
    }

    @Test
    @DisplayName("Test calculateNextMoveFor with enemy castle node")
    public void testCalculateNextMove5() {
        map.getFieldForCoordinates(3,3).gameFortState = GameMap.GameFortState.EnemyFortPresent;
        pathFinder.setMap(map);

        Mockito.when(shortestPathFinder.calculateShortestPathBetween(Mockito.any(GraphNode.class), Mockito.any(GraphNode.class), Mockito.any(MapGraph.class)))
                .thenReturn(List.of(graph.getNode(map.getFieldForCoordinates(0,0))));

        Mockito.when(mapExplorer.getMovesFor(Mockito.anyList()))
                .thenReturn(new LinkedList<>(Arrays.asList(GameMove.Left)));

        assertEquals(pathFinder.calculateNextMoveFor(map.getFieldForCoordinates(0,0)), GameMove.Left);

        Mockito.verify(shortestPathFinder).calculateShortestPathBetween(
                argThat((GraphNode graphNode) ->
                        graphNode.getField().x.equals(0) && graphNode.getField().y.equals(0)),
                argThat((GraphNode graphNode) ->
                        graphNode.getField().x.equals(3) && graphNode.getField().y.equals(3)),
                argThat((MapGraph graph) ->
                        true));
    }

    @Test
    @DisplayName("Test set bounds treasure not found")
    public void testSetBounds() {
        assertEquals(pathFinder.getTopLeftBound().x, 0);
        assertEquals(pathFinder.getTopLeftBound().y, 0);
        assertEquals(pathFinder.getBottomRightBound().x, 7);
        assertEquals(pathFinder.getBottomRightBound().y, 3);
    }

    @Test
    @DisplayName("Test set bounds treasure was found")
    public void testSetBoundsTreasureFound() {
        pathFinder.setHasCollectedTreasure(true);
        assertEquals(pathFinder.getTopLeftBound().x, 0);
        assertEquals(pathFinder.getTopLeftBound().y, 4);
        assertEquals(pathFinder.getBottomRightBound().x, 7);
        assertEquals(pathFinder.getBottomRightBound().y, 7);
    }
}
