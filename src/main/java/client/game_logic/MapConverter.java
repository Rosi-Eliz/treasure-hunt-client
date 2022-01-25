package client.game_logic;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import client.error_handling.MapConvertionException;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MapConverter {
    private static HalfMapNode generateHalfMapNode(GameMap.GameField field) {
        ETerrain terrain = null;
        switch(field.terrain)
        {
            case Grass: terrain =  ETerrain.Grass; break;
            case Water: terrain = ETerrain.Water; break;
            case Mountain: terrain = ETerrain.Mountain; break;
            default: throw new MapConvertionException("Unknown terrain type");

        }
        boolean castlePresent = field.gameFortState == GameMap.GameFortState.MyFortPresent;
        return new HalfMapNode(field.x, field.y, castlePresent, terrain);
    }

    private static GameMap.GameField generateFieldFromNode(FullMapNode fullMapNode) {
        GameMap.Terrain terrain = null;
        switch(fullMapNode.getTerrain())
        {
            case Grass: terrain =  GameMap.Terrain.Grass; break;
            case Water: terrain = GameMap.Terrain.Water; break;
            case Mountain: terrain = GameMap.Terrain.Mountain; break;
            default: throw new MapConvertionException("Unknown terrain type");
        }

        GameMap.PlayerPositionState playerPos = null;
        switch(fullMapNode.getPlayerPositionState())
        {
            case NoPlayerPresent: playerPos = GameMap.PlayerPositionState.NoPlayerPresent; break;
            case EnemyPlayerPosition: playerPos = GameMap.PlayerPositionState.EnemyPlayerPosition; break;
            case MyPlayerPosition: playerPos = GameMap.PlayerPositionState.MyPlayerPosition; break;
            case BothPlayerPosition: playerPos = GameMap.PlayerPositionState.BothPlayerPosition; break;
            default: throw new MapConvertionException("Unknown player position state");
        }

        GameMap.GameTreasureState treasureState = null;
        switch(fullMapNode.getTreasureState())
        {
            case NoOrUnknownTreasureState: treasureState = GameMap.GameTreasureState.NoOrUnknownTreasureState; break;
            case MyTreasureIsPresent: treasureState = GameMap.GameTreasureState.MyTreasureIsPresent; break;
        }

        GameMap.GameFortState fortState = null;
        switch(fullMapNode.getFortState())
        {
            case NoOrUnknownFortState: fortState = GameMap.GameFortState.NoOrUnknownFortState; break;
            case MyFortPresent: fortState = GameMap.GameFortState.MyFortPresent; break;
            case EnemyFortPresent: fortState = GameMap.GameFortState.EnemyFortPresent; break;

        }
        return new GameMap.GameField(fullMapNode.getX(), fullMapNode.getY(), terrain, playerPos, treasureState, fortState);
    }

    public static HalfMap generateHalfMap(String uniquePlayerID, GameMap gameMap){
            List<HalfMapNode> transformedFields = gameMap.getAllFields().stream().map(MapConverter::generateHalfMapNode).collect(Collectors.toList());
            return new HalfMap(uniquePlayerID, transformedFields);
    }

    public static GameMap generateGameMap(FullMap fullMap)
    {
        int rows = fullMap.getMapNodes().stream().max(Comparator.comparingInt(FullMapNode::getX)).orElseThrow().getX() + 1;
        int columns = fullMap.getMapNodes().stream().max(Comparator.comparingInt(FullMapNode::getY)).orElseThrow().getY() + 1;
        GameMap gameMap = new GameMap(rows, columns);
        for(FullMapNode node : fullMap.getMapNodes())
        {
            gameMap.setField(generateFieldFromNode(node));
        }
        return gameMap;
    }
}
