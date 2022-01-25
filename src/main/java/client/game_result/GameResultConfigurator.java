package client.game_result;

import client.game_logic.GameLogicResult;

public class GameResultConfigurator {
    private GameResultView gameResultView;
    private GameLogicResult gameLogicResult;

    public GameResultConfigurator(GameLogicResult gameLogicResult) {
        this.gameLogicResult = gameLogicResult;
    }

    public void configureAndInitialize()
    {
        gameResultView = new GameResultView(gameLogicResult);
        gameResultView.draw();
    }

}
