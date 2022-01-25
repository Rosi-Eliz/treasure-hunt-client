package client.game_logic;

import MessagesBase.*;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.FullMap;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import client.AI.AIEngineMapGeneratable;
import client.AI.PathFinder.AIEnginePathFindable;
import client.AI.PathFinder.GameMove;
import client.error_handling.NetworkCommunicationException;
import client.game_logic.MVC.IGameLogicModel;
import client.network.NetworkManagable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameLogicModel implements IGameLogicModel {
    private static final int interCheckStatusRequestsMilisecondsDelay = 4;

    private AIEngineMapGeneratable mapGenerator;
    private AIEnginePathFindable pathFinder;

    private String playerID;
    private String gameID;
    private GameLogicNetworkHandler networkHandler;
    private Timer timer = new Timer();
    private static Logger LOGGER = LoggerFactory.getLogger(GameLogicModel.class);
    private boolean didSendHalfMap = false;
    private final PropertyChangeSupport gameChanges = new PropertyChangeSupport(this);
    private GameLogicModelOutput output;

    public GameLogicModel(NetworkManagable networkManager,
                          AIEngineMapGeneratable aiEngine,
                          AIEnginePathFindable pathFinder,
                          String playerID,
                          String gameID) {
        this.mapGenerator = aiEngine;
        this.pathFinder = pathFinder;
        this.playerID = playerID;
        this.gameID = gameID;
        networkHandler = new GameLogicNetworkHandler(networkManager);
    }

    public void setOutput(GameLogicModelOutput output) {
        this.output = output;
    }

    public void addResultChangeListener(PropertyChangeListener listener){
        gameChanges.addPropertyChangeListener(listener);
    }

    @Override
    public void generateAndProcessHalfMap() throws NetworkCommunicationException {
        GameMap generatedMap = mapGenerator.generateHalfMap();
        generatedMap.print();
        HalfMap halfMap = MapConverter.generateHalfMap(playerID, generatedMap);
        ResponseEnvelope<ERequestState> request = networkHandler.sendHalfMap(gameID, halfMap);
        if (request.getState() == ERequestState.Error) {
            throw new NetworkCommunicationException(request.getExceptionMessage());
        }
    }

    @Override
    public void startPlaying(){
        try{
            checkGameStatus();
        } catch (NetworkCommunicationException e){
            LOGGER.error("Error when checking the game status {}", e.getMessage());
        }
    }

    private void checkGameStatus() throws NetworkCommunicationException {
        ResponseEnvelope<GameState> request = networkHandler.getGameStatus(gameID, playerID);
        if (request.getState() == ERequestState.Error) {
            throw new NetworkCommunicationException(request.getExceptionMessage());
        }
        Optional<FullMap> map = request.getData().get().getMap();
            try {
                PlayerState playerState = request.getData().get().getPlayers()
                        .parallelStream()
                        .filter(player -> player.getUniquePlayerID().equals(playerID))
                        .findFirst()
                        .get();
                EPlayerGameState gameStateID = playerState.getState();

                LOGGER.debug("gameState is {}", gameStateID);

                switch (gameStateID) {
                    case Lost:
                        output.didReceiveGameLogicResult(new GameLogicResult(null, false));
                        return;
                    case Won:
                        output.didReceiveGameLogicResult(new GameLogicResult(null, true));
                        return;
                    case MustAct:
                        if(!didSendHalfMap) {
                            generateAndProcessHalfMap();
                            didSendHalfMap = true;
                        } else if (map.isPresent()){
                            generateNextMove(map.get(), playerState.hasCollectedTreasure());
                        } else {
                            LOGGER.error("Map is null and MustAct!");
                        }
                        break;
                    case MustWait:
                        break;
                    default:
                        LOGGER.error("Unknown game state {}", gameStateID);
                        return;
                }
            } catch (NoSuchElementException e) {
                output.didReceiveGameLogicResult(new GameLogicResult(e.getMessage(), false));
                LOGGER.error("Networking error {}", e.getMessage());
            }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkGameStatus();
                } catch (NetworkCommunicationException e) {
                    LOGGER.error("Error when checking game status {}", e.getMessage());
                }
            }
        }, interCheckStatusRequestsMilisecondsDelay * 100);
    }

    private void generateNextMove(FullMap map, boolean hasCollectedTreasure) throws NetworkCommunicationException {
        GameMap gameMap = MapConverter.generateGameMap(map);
        gameChanges.firePropertyChange("fetchedMapResult", null, gameMap);
        pathFinder.setHasCollectedTreasure(hasCollectedTreasure);
        pathFinder.setMap(gameMap);
        GameMap.GameField currentPosition = gameMap.myPlayerCurrentPosition();
        if(currentPosition == null)
        {
            LOGGER.error("Error when getting current position");
            return;
        }
        GameMove move = pathFinder.calculateNextMoveFor(gameMap.myPlayerCurrentPosition());
        EMove emove = null;
        switch (move) {
            case Up:    emove = EMove.Up; break;
            case Down:  emove = EMove.Down; break;
            case Left:  emove = EMove.Left; break;
            case Right: emove = EMove.Right; break;
        }
        LOGGER.debug("Performed {} move", emove);
        PlayerMove playerMove = PlayerMove.of(playerID, emove);
        ResponseEnvelope<ERequestState> request = networkHandler.sendMove(playerMove, gameID);
        if (request.getState() == ERequestState.Error) {
            throw new NetworkCommunicationException(request.getExceptionMessage());
        }
    }
}