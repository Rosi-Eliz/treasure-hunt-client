package client.game_logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameMapTests {
    private GameMap map;

    @BeforeEach
    public void setup() {
        map = new GameMap(8, 4);
        map.getAllFields().stream().forEach(field -> field.terrain = GameMap.Terrain.Grass);
    }

    @ParameterizedTest
    @CsvSource({"0,0,2", "2,2,4", "7,3,2"})
    @DisplayName("Test get neighbours for field")
    public void testGetNeighboursForField(int x, int y, int expectedNeighboursCount) {
       assertEquals(map.getNeighboursForField(x, y).size(), expectedNeighboursCount);
    }
}
