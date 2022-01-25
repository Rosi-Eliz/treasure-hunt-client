package client.game_logic;

import client.game_logic.MVC.IGameLogicController;

public class GameLogicController implements IGameLogicController, GameLogicModelOutput {
    private GameLogicModel model;
    private GameLogicControllerOutput output;

    public GameLogicController(GameLogicModel model, GameLogicControllerOutput output) {
        this.model = model;
        this.output = output;
    }

    @Override
    public void initiateGameLogic() {
        model.startPlaying();
    }

    @Override
    public void didReceiveGameLogicResult(GameLogicResult gameLogicResult) {
        output.didReceiveGameLogicResult(gameLogicResult);
    }
}
