package client.AI;

import client.error_handling.MapGenerationException;
import client.game_logic.GameMap;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIEngineMapGenerator implements AIEngineMapGeneratable {
    private GameMap map;
    private int minGrass = 15;
    private int minMountains = 3;
    private int minWater = 4;
    private Random rand = new Random();
    private static final int MAX_HORIZONTAL_WATER_FIELDS = 3;
    private static final int MAX_VERTICAL_WATER_FIELDS = 1;
    private static final int mapColumns = 8;
    private static final int mapRows = 4;
    private static Logger LOGGER = LoggerFactory.getLogger(AIEngineMapGenerator.class);

    public AIEngineMapGenerator() {
        map = new GameMap(mapColumns, mapRows);
    }

    private void processTerrainTypeAssignment(GameMap.Terrain terrainType) {
        switch(terrainType)
        {
            case Grass:  minGrass--; break;
            case Mountain: minMountains--; break;
            case Water:  minWater--; break;
            default: throw new MapGenerationException("Invalid terrain type");
        }
    }
    private boolean typeHasAllTheRequiredFields(GameMap.Terrain terrainType)
    {
        switch(terrainType)
        {
            case Grass: return minGrass == 0;
            case Mountain: return minMountains == 0;
            case Water: return minWater == 0;
            default: throw new MapGenerationException("Invalid terrain type");
        }
    }

    private int getRemainingRequiredSlots() {
        return minWater + minMountains + minGrass;
    }

    private void assignRequiredFields() {
        LOGGER.info("Map generation begins");
        while (getRemainingRequiredSlots() != 0) {
            List<GameMap.GameField> unassignedFields = map.getUnassignedFields();
            GameMap.GameField randomUnassignedField = unassignedFields.get(rand.nextInt(unassignedFields.size() - 1));
            GameMap.Terrain randomTerrain;
            do {
                randomTerrain = GameMap.Terrain.getTypeForIndex(rand.nextInt(GameMap.Terrain.values().length - 1));
                if (randomTerrain == GameMap.Terrain.Water &&
                        (map.areThereNeighbouringIslandsForWaterAt(randomUnassignedField) ||
                                !isBorderWaterConditionMet(randomUnassignedField)) ||
                        doesWaterFieldCreateSeparation(randomUnassignedField)) {
                    randomTerrain = GameMap.Terrain.Unassigned;
                }
            } while (randomTerrain == GameMap.Terrain.Unassigned ||
                    (typeHasAllTheRequiredFields(randomTerrain) &&
                            getRemainingRequiredSlots() > 0) );

            processTerrainTypeAssignment(randomTerrain);
            randomUnassignedField.terrain = randomTerrain;
            map.setField(randomUnassignedField);
        }
        assignRemainingFields();
        placeCastle();
        LOGGER.info("Map generation ends!");
    }

    private void placeCastle()
    {
        List<GameMap.GameField> grassFields = map.getFieldsByTerrain(GameMap.Terrain.Grass);
        GameMap.GameField castleField = grassFields.get(rand.nextInt(grassFields.size() - 1));

        castleField.gameFortState = GameMap.GameFortState.MyFortPresent;
        castleField.playerPositionState = GameMap.PlayerPositionState.MyPlayerPosition;
        castleField.gameTreasureState = GameMap.GameTreasureState.NoOrUnknownTreasureState;
        map.setField(castleField);
    }

    private void assignRemainingFields() {
        List<GameMap.GameField> unassignedFields = map.getUnassignedFields();
        for (GameMap.GameField field : unassignedFields) {
            field.terrain = GameMap.Terrain.Grass;
            map.setField(field);
        }
    }

    private boolean doesWaterFieldCreateSeparation(GameMap.GameField field) {
        if (minWater == 1)
        {
            GameMap.Terrain oldTerrain = field.terrain;
            field.terrain = GameMap.Terrain.Water;
            map.setField(field);
            var allWaterFields = map.getFieldsByTerrain(GameMap.Terrain.Water)
                    .stream()
                    .sorted((field1, field2) -> field1.y - field2.y)
                    .collect(Collectors.toList());

            var yCoordinates = allWaterFields.stream().map(f -> f.y).collect(Collectors.toList());
            if(yCoordinates.stream().distinct().count() == 4)
            {
                var tempField = allWaterFields.get(0);
                for(var f : allWaterFields)
                {
                    if(Math.abs(tempField.x - f.x) > 1)
                    {
                        field.terrain = oldTerrain;
                        map.setField(field);
                        return false;
                    }
                    tempField = f;
                }
                field.terrain = oldTerrain;
                map.setField(field);
                return true;
            }
            field.terrain = oldTerrain;
            map.setField(field);
            return false;
        }
        return false;
    }

    private boolean borderConditionsWaterMet(List<GameMap.GameField> fields)
    {
        int horizontalWaters = 0;
        int verticalWaters  = 0;
        for(GameMap.GameField field : fields)
        {
            if(field.x == 0 || field.x == mapColumns - 1)
                verticalWaters++;
            if(field.y == 0 || field.y == mapRows - 1)
                horizontalWaters++;
        }
        return horizontalWaters < MAX_HORIZONTAL_WATER_FIELDS && verticalWaters < MAX_VERTICAL_WATER_FIELDS;
    }

    private boolean isBorderWaterConditionMet(GameMap.GameField field)
    {
        if(map.isFieldBorderLocated(field))
        {
            GameMap.Terrain oldTerrain = field.terrain;
            field.terrain = GameMap.Terrain.Water;
            map.setField(field);

            List<GameMap.GameField> fields = map.getWaterFieldsAtBorderFor(field);
            boolean result = borderConditionsWaterMet(fields);

            field.terrain = oldTerrain;
            map.setField(field);
            return result;
        }
        return true;
    }

    @Override
    public GameMap generateHalfMap() {
        assignRequiredFields();
        return map;
    }
}
