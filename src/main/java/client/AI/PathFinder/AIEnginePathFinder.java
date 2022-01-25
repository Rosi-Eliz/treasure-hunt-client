package client.AI.PathFinder;
import client.error_handling.MapExplorationException;
import client.game_logic.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;

public class AIEnginePathFinder implements AIEnginePathFindable{
    private GameMap map;
    private MapGraph graph;
    private Queue<GameMove> moves = new LinkedList<>();
    private AIEngineShortestPathFindable shortestPathFinder;
    private AIEngineExplorable mapExplorer;
    private boolean hasCollectedTreasure = false;
    private Point myHalfTopLeftBound;
    private Point myHalfBottomRightBound;
    private Point enemyHalfTopLeftBound;
    private Point enemyHalfBottomRightBound;

    private static Logger LOGGER = LoggerFactory.getLogger(AIEnginePathFinder.class);

    public AIEnginePathFinder(AIEngineShortestPathFindable shortestPathFinder,
                              AIEngineExplorable mapExplorer) {
        this.shortestPathFinder = shortestPathFinder;
        this.mapExplorer = mapExplorer;
    }

    public Point getTopLeftBound() {
        return hasCollectedTreasure ? enemyHalfTopLeftBound : myHalfTopLeftBound;
    }

    public Point getBottomRightBound() {
        return hasCollectedTreasure ? enemyHalfBottomRightBound : myHalfBottomRightBound;
    }

    private void configureBounds() {
        var myPosition  = map.myPlayerCurrentPosition();
        boolean mapIsConstructedHorizontally = map.getColumns() > map.getRows();
        if(mapIsConstructedHorizontally) {
            boolean isLocatedInLeftHalf = myPosition.x < map.getColumns() / 2;
            int halfWidth = map.getColumns() / 2;
            int halfHeight = map.getRows();
            if(isLocatedInLeftHalf) {
                myHalfTopLeftBound = new Point(0, 0);
                myHalfBottomRightBound = new Point(halfWidth - 1, halfHeight - 1);
                enemyHalfTopLeftBound = new Point(halfWidth, 0);
                enemyHalfBottomRightBound = new Point(map.getColumns() - 1, halfHeight - 1);
            } else {
                myHalfTopLeftBound = new Point(halfWidth, 0);
                myHalfBottomRightBound = new Point(map.getColumns() - 1, halfHeight - 1);
                enemyHalfTopLeftBound = new Point(0, 0);
                enemyHalfBottomRightBound = new Point(halfWidth - 1, halfHeight - 1);
            }
        } else {
            boolean isLocatedInTopHalf = myPosition.y < map.getRows() / 2;
            int halfWidth = map.getColumns();
            int halfHeight = map.getRows() / 2;
            if(isLocatedInTopHalf) {
                myHalfTopLeftBound = new Point(0, 0);
                myHalfBottomRightBound = new Point(halfWidth - 1, halfHeight - 1);
                enemyHalfTopLeftBound = new Point(0, halfHeight);
                enemyHalfBottomRightBound = new Point(halfWidth - 1, map.getRows() - 1);
            } else {
                myHalfTopLeftBound = new Point(0, halfHeight);
                myHalfBottomRightBound = new Point(halfWidth - 1, map.getRows() - 1);
                enemyHalfTopLeftBound = new Point(0, 0);
                enemyHalfBottomRightBound = new Point(halfWidth - 1, halfHeight - 1);
            }
        }
    }

    private GraphNode getClosestUnvisitedNode(GraphNode currentPosition, Point topLeftBound, Point bottomRightBound) {
        return graph.getUnvisitedNodes().stream()
                .filter(node -> GraphUtilities.isNodeInBounds(node, topLeftBound, bottomRightBound))
                .min((node1, node2) -> Integer.compare(abs(currentPosition.getField().x - node1.getField().x) + abs(currentPosition.getField().y - node1.getField().y),
                        abs(currentPosition.getField().x - node2.getField().x) + abs(currentPosition.getField().y - node2.getField().y)))
                .orElse(null);
    }

    private Queue<GameMove> generateMoves(GraphNode start, GraphNode end) {
        var nodes = shortestPathFinder.calculateShortestPathBetween(start, end, graph);
        if(nodes.isEmpty() || !nodes.get(0).equals(start)) {
            LOGGER.error("No path found between {} and {}", start, end);
            return new LinkedList<>();
        }
        return mapExplorer.getMovesFor(nodes);
    }

    @Override
    public GameMove calculateNextMoveFor(GameMap.GameField field) {
        if(map == null || graph == null) {
            throw new MapExplorationException("Map is not set");
        }
        if(!moves.isEmpty()) {
            LOGGER.info("Client performed cached move {}", moves.peek());
            return moves.poll();
        }
        GameMap.GameField treasureField = map.myTreasureField();
        GameMap.GameField enemyCastleField = map.enemyCastleField();

        if(treasureField != null) {
            LOGGER.debug("Client received my treasure field");
        }
        if(enemyCastleField != null) {
            LOGGER.debug("Client received enemy castle field");
        }

        if (treasureField != null) {
            moves = generateMoves(graph.getNode(field), graph.getNode(treasureField));
        } else if (enemyCastleField != null) {
            moves = generateMoves(graph.getNode(field), graph.getNode(enemyCastleField));
        } else {
            moves = mapExplorer.exploreMap(graph.getNode(field), getTopLeftBound(), getBottomRightBound());
            if(moves == null)
            {
                var closestUnvisitedNode = getClosestUnvisitedNode(graph.getNode(field), getTopLeftBound(), getBottomRightBound());
                if(closestUnvisitedNode != null) {
                    moves = generateMoves(graph.getNode(field), closestUnvisitedNode);
                } else {
                    LOGGER.error("Unable to find unvisited node");
                }
            }
        }
        LOGGER.info("Client performed cached move {}", moves.peek());
        return moves.poll();
    }

    private void didReceiveMapUpdate(GameMap map) {
        var currentPosition = map.myPlayerCurrentPosition();
        graph.getNode(currentPosition).setIsDiscovered(true);
        if(currentPosition.terrain == GameMap.Terrain.Mountain)
        {
            List<GameMap.GameField> surroundingFields = map.getSurroundingFields(currentPosition.x , currentPosition.y);
            for(GameMap.GameField field : surroundingFields)
            {
                var node = graph.getNode(field);
                if(node != null && node.getField().terrain != GameMap.Terrain.Mountain &&
                        node.getField().gameTreasureState != GameMap.GameTreasureState.MyTreasureIsPresent &&
                        node.getField().gameFortState != GameMap.GameFortState.EnemyFortPresent)
                {
                    node.setIsDiscovered(true);
                }
            }
        }

        GameMap.GameField myTreasureField = map.myTreasureField();
        if(myTreasureField != null) {
            GameMap.GameField fieldForUpdate = this.map.getFieldForCoordinates(myTreasureField.x, myTreasureField.y);
            fieldForUpdate.gameTreasureState = GameMap.GameTreasureState.MyTreasureIsPresent;
            this.map.setField(fieldForUpdate);
        } else {
            var storedTreasureField = this.map.myTreasureField();
            if(storedTreasureField != null) {
                storedTreasureField.gameTreasureState = GameMap.GameTreasureState.NoOrUnknownTreasureState;
                this.map.setField(storedTreasureField);
            }
        }

        GameMap.GameField enemyCastleField = map.enemyCastleField();
        if(enemyCastleField != null) {
            GameMap.GameField fieldForUpdate = this.map.getFieldForCoordinates(enemyCastleField.x, enemyCastleField.y);
            fieldForUpdate.gameFortState = GameMap.GameFortState.EnemyFortPresent;
            this.map.setField(fieldForUpdate);
        }
    }

    @Override
    public void setMap(GameMap map) {
        this.map = map;
        if(graph == null) {
            configureBounds();
            graph = new MapGraph(this.map);
            mapExplorer.setGraph(graph);
        }
        didReceiveMapUpdate(map);
    }

    @Override
    public void setHasCollectedTreasure(boolean hasCollectedTreasure) {
        this.hasCollectedTreasure = hasCollectedTreasure;
    }
}
