package client.game_logic;
import client.game_logic.MVC.IGameLogicView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;

public class GameLogicView implements IGameLogicView {
    private GameLogicController controller;
    private static Logger LOGGER = LoggerFactory.getLogger(GameLogicView.class);
    private GameBoard board;

    private PropertyChangeListener modelChangeListener = event -> {
        Object data = event.getNewValue();
        if (data instanceof GameMap) {
            draw((GameMap)data);
        } else {
            throw new IllegalArgumentException("Unknown data type");
        }
    };

    public GameLogicView(GameLogicController controller, GameLogicModel model) {
        this.controller = controller;
        model.addResultChangeListener(modelChangeListener);
    }

    @Override
    public void draw(GameMap map) {
        if(board == null) {
            board = new GameBoard(map);
        } else {
            board.updateBoard(map);
        }
        map.print();
    }
}
