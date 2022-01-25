package client.AI.PathFinder;

import client.game_logic.GameMap;

public interface AIEnginePathFindable {
    GameMove calculateNextMoveFor(GameMap.GameField field);
    void setMap(GameMap map);
    void setHasCollectedTreasure(boolean hasCollectedTreasure);
}
