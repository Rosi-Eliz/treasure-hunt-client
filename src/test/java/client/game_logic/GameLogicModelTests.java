package client.game_logic;

import MessagesBase.*;
import client.AI.AIEngineMapGeneratable;
import client.AI.PathFinder.AIEnginePathFindable;
import client.error_handling.NetworkCommunicationException;
import client.network.NetworkManagable;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.util.LinkedMultiValueMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;

public class GameLogicModelTests {
    private NetworkManagable networkManager;
    private AIEnginePathFindable pathFinder;
    private AIEngineMapGeneratable mapGenerator;
    private GameLogicModel model;
    @BeforeEach
    public void setup() {
        networkManager = Mockito.mock(NetworkManagable.class);
        pathFinder = Mockito.mock(AIEnginePathFindable.class);
        mapGenerator = Mockito.mock(AIEngineMapGeneratable.class);
        model = new GameLogicModel(networkManager, mapGenerator, pathFinder, "123", "456");
    }

    @Test
    @DisplayName("Test generate and process half map")
    public void testGetMap() throws NetworkCommunicationException {
        GameMap map = new GameMap(8,4);
        map.getAllFields().stream().forEach(field -> field.terrain = GameMap.Terrain.Grass);
        Mockito.when(mapGenerator.generateHalfMap()).thenReturn(map);
        final ResponseEnvelope response = new ResponseEnvelope<ERequestState>();
        Mockito.when(networkManager.postRequest(any(HalfMap.class), anyString(), any(LinkedMultiValueMap.class)))
                .thenReturn(response);

        assertDoesNotThrow(() -> {
            model.generateAndProcessHalfMap();
        });
    }
}
