package client.coordinator;

import client.AI.AIEngineMapGenerator;
import client.AI.AIEngineMapGeneratable;
import client.AI.PathFinder.AIEngineMapExplorer;
import client.AI.PathFinder.AIEnginePathFindable;
import client.AI.PathFinder.AIEnginePathFinder;
import client.AI.PathFinder.AIEngineShortestPath;
import client.game_logic.GameLogicConfigurator;
import client.game_logic.GameLogicControllerOutput;
import client.game_logic.GameLogicResult;
import client.game_result.GameResultConfigurator;
import client.network.NetworkManager;
import client.player_registration.PlayerRegistrationConfigurator;
import client.player_registration.PlayerRegistrationControllerOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Coordinator implements Coordinatable,
        PlayerRegistrationControllerOutput,
        GameLogicControllerOutput {
    private NetworkManager networkManager;
    private PlayerRegistrationConfigurator playerRegistrationConfigurator;
    private GameResultConfigurator gameResultConfigurator;
    private GameLogicConfigurator gameLogicConfigurator;
    private AIEngineMapGeneratable aiEngine = new AIEngineMapGenerator();
    private AIEnginePathFindable pathFinder = new AIEnginePathFinder(new AIEngineShortestPath(), new AIEngineMapExplorer());
    private String gameID;
    private static Logger LOGGER = LoggerFactory.getLogger(Coordinator.class);
    @Override
    public void initiate(List<String> inputParameters){
        if(inputParameters.size() == 2)
        {
            this.networkManager = new NetworkManager(inputParameters.get(0));
            gameID = inputParameters.get(1);
            initiatePlayerRegistrationFlow(inputParameters.get(0), inputParameters.get(1));
        } else {
            LOGGER.error("Invalid number of input parameters");
        }
    }

    //Coordinator invocations
    private void initiatePlayerRegistrationFlow(String serverURl, String gameID)
    {
        playerRegistrationConfigurator = new PlayerRegistrationConfigurator(networkManager,this, gameID);
        playerRegistrationConfigurator.configureAndInitialize();
    }

    private void initiateGameLogicFlow(String playerID)
    {
        gameLogicConfigurator = new GameLogicConfigurator(networkManager, this, gameID, aiEngine, pathFinder, playerID);
        gameLogicConfigurator.configureAndInitialize();
    }

    //Coordinator callbacks:
    @Override
    public void didRegisterPlayer(String playerID) {
        initiateGameLogicFlow(playerID);
    }

    @Override
    public void didReceiveGameLogicResult(GameLogicResult gameLogicResult) {
        gameResultConfigurator = new GameResultConfigurator(gameLogicResult);
        gameResultConfigurator.configureAndInitialize();
    }
}
