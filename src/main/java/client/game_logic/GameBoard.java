package client.game_logic;

import javax.swing.*;
import java.awt.*;

import static client.game_logic.GameMap.GameTreasureState.*;

public class GameBoard extends JFrame {
    private final int spacing = 5;
    private final int fieldSize = 80;
    private int width;
    private int height;
    private GameMap map;

    public GameBoard(GameMap map) {
        width = map.getColumns() * fieldSize + (map.getColumns() - 1) * spacing + 2 * spacing;
        height = map.getRows() * fieldSize + (map.getRows() - 1) * spacing + 2 * spacing;
        setTitle("Game Map");
        setSize(width, height);
        this.map = map;
        setVisible(true);
        setResizable(false);
        repaint();
    }

    public void updateBoard(GameMap map)
    {
        this.map = map;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        for(int y = 0; y < map.getRows(); y++) {
            for (int x = 0; x < map.getColumns(); x++) {

                GameMap.GameField field = map.getFieldForCoordinates(x, y);

                g.setColor(getColorForField(field));
                g.fillRect(spacing + x * (fieldSize + spacing),
                        spacing + y * (fieldSize + spacing), fieldSize, fieldSize);
            }
        }
    }

    private Color getColorForField(GameMap.GameField field) {
        if (field.playerPositionState == GameMap.PlayerPositionState.MyPlayerPosition) {
            return Color.CYAN;
        } else if (field.playerPositionState == GameMap.PlayerPositionState.EnemyPlayerPosition) {
            return Color.RED;
        }

        if(field.gameFortState == GameMap.GameFortState.MyFortPresent)
        {
            return new Color(51, 0, 0);
        } else if(field.gameFortState == GameMap.GameFortState.EnemyFortPresent) {
            return new Color(153,102,0);
        }

        if (field.gameTreasureState == MyTreasureIsPresent) {
            return Color.ORANGE;
        } else if (field.gameTreasureState == EnemyTreasureIsPresent) {
            return Color.PINK;
        }

        switch (field.terrain) {
            case Grass:
                return Color.GREEN;
            case Water:
                return Color.BLUE;
            case Mountain:
                return Color.GRAY;
        }
        return Color.BLACK;
    }
}
