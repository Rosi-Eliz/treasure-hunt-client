package client.game_logic;

import client.AI.AIEngineMapGeneratable;
import client.AI.PathFinder.AIEnginePathFindable;
import client.network.NetworkManagable;

public class GameLogicConfigurator {
    private NetworkManagable networkManager;
    private GameLogicModel model;
    private GameLogicView view;
    private GameLogicController controller;
    private GameLogicControllerOutput output;
    private String gameID;
    private AIEngineMapGeneratable aiEngine;
    private AIEnginePathFindable pathFinder;
    private String playerID;

    public GameLogicConfigurator(NetworkManagable networkManager,
                                 GameLogicControllerOutput output,
                                 String gameID,
                                 AIEngineMapGeneratable aiEngine,
                                 AIEnginePathFindable pathFinder,
                                 String playerID) {
        this.networkManager = networkManager;
        this.output = output;
        this.gameID = gameID;
        this.aiEngine = aiEngine;
        this.pathFinder = pathFinder;
        this.playerID = playerID;
    }

    public void configureAndInitialize() {
        model = new GameLogicModel(networkManager, aiEngine, pathFinder, playerID, gameID);
        view = new GameLogicView(controller, model);
        controller = new GameLogicController(model, output);
        model.setOutput(controller);
        controller.initiateGameLogic();
    }
}
