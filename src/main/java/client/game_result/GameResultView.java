package client.game_result;

import client.game_logic.GameLogicResult;
import client.game_result.MVC.IGameResultView;
import client.player_registration.PlayerRegistrationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public class GameResultView implements IGameResultView {
    private GameLogicResult gameLogicResult;

    public GameResultView(GameLogicResult gameLogicResult) {
        this.gameLogicResult = gameLogicResult;
    }

    public void draw() {
        JOptionPane.showMessageDialog(null, gameLogicResult.getMessage());
        System.out.println(gameLogicResult.getMessage());
    }
}
