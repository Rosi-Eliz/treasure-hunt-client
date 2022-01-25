package client.AI;

import client.game_logic.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AIEngineMapGenertatorTests {
    private AIEngineMapGenerator aiEngineMapGenerator;

    @BeforeEach
    public void setUp() {
        aiEngineMapGenerator = new AIEngineMapGenerator();
    }

    @Test
    @DisplayName("Test generate half map")
    public void testGenerateHalfMap() {
        GameMap map = aiEngineMapGenerator.generateHalfMap();
        assertEquals(map.getRows(), 4);
        assertEquals(map.getColumns(), 8);
        assertTrue(map.isMapInitialised());
        assertTrue(map.getUnassignedFields().isEmpty());
        assertEquals(map.getFieldsByTerrain(GameMap.Terrain.Water).size(), 4);
        assertEquals(map.getFieldsByTerrain(GameMap.Terrain.Mountain).size(), 3);
        assertTrue(map.getFieldsByTerrain(GameMap.Terrain.Grass).size() > 15);
    }

    @Test
    @DisplayName("Test map correctness")
    public void testMapCorrectness() {
        GameMap map = aiEngineMapGenerator.generateHalfMap();
        assertEquals(map.getSurroundingFields(0,0).size(), 3);
        assertEquals(map.getSurroundingFields(2,2).size(), 8);
        assertEquals(map.getSurroundingFields(7,3).size(), 3);

        assertEquals(map.getNeighboursForField(0,0).size(), 2);
        assertEquals(map.getNeighboursForField(2,2).size(), 4);
        assertEquals(map.getNeighboursForField(7,3).size(), 2);

        GameMap.GameField field1 = map.getFieldForCoordinates(1,0);
        field1.terrain = GameMap.Terrain.Water;
        GameMap.GameField field2 = map.getFieldForCoordinates(0,1);
        field2.terrain = GameMap.Terrain.Water;
        assertTrue(map.isFieldSurroundedByWater(map.getFieldForCoordinates(0,0)));
        assertTrue(map.areThereNeighbouringIslandsForWaterAt(field1));
        assertTrue(map.areThereNeighbouringIslandsForWaterAt(field2));

        assertTrue(map.isFieldBorderLocated(map.getFieldForCoordinates(0,0)));
        assertTrue(map.isFieldBorderLocated(map.getFieldForCoordinates(7,3)));
        assertTrue(map.isFieldBorderLocated(map.getFieldForCoordinates(3,3)));

        assertFalse(map.isFieldBorderLocated(map.getFieldForCoordinates(2,2)));
        assertFalse(map.isFieldBorderLocated(map.getFieldForCoordinates(1,1)));
        assertFalse(map.isFieldBorderLocated(map.getFieldForCoordinates(2,1)));

        assertEquals(map.getSequenceOrientation(map.getFieldForCoordinates(0,0)), GameMap.SequenceOrientation.Both);
        assertEquals(map.getSequenceOrientation(map.getFieldForCoordinates(0,2)), GameMap.SequenceOrientation.Vertical);
        assertEquals(map.getSequenceOrientation(map.getFieldForCoordinates(5,0)), GameMap.SequenceOrientation.Horizontal);
        assertEquals(map.getSequenceOrientation(map.getFieldForCoordinates(2,2)), GameMap.SequenceOrientation.None);

        assertNull(map.myTreasureField());
        assertNull(map.myTreasureField());
        assertNotNull(map.myPlayerCurrentPosition());
    }

    @Test
    @DisplayName("Test water validation")
    public void testWaterValidation(){
        GameMap map = aiEngineMapGenerator.generateHalfMap();
        assertEquals(map.getALlFieldsExcludingWaters().size(), 28);

        for(var waterField : map.getFieldsByTerrain(GameMap.Terrain.Water)){
            waterField.terrain = GameMap.Terrain.Grass;
        }
        map.getFieldForCoordinates(0,0).terrain = GameMap.Terrain.Water;
        map.getFieldForCoordinates(3,0).terrain = GameMap.Terrain.Water;
        map.getFieldForCoordinates(2,2).terrain = GameMap.Terrain.Water;
        map.getFieldForCoordinates(0,2).terrain = GameMap.Terrain.Water;
        assertEquals(map.getWaterFieldsAtBorderFor(map.getFieldForCoordinates(0,0)).size(), 3);

        assertEquals(map.getAllNeighboursExceptWater(map.getFieldForCoordinates(1,0)).size(), 2 );
    }
}
