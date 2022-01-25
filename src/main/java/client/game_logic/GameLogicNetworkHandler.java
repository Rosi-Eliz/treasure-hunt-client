package client.game_logic;
import MessagesBase.*;
import MessagesGameState.FullMap;
import MessagesGameState.GameState;
import client.network.NetworkManagable;
import org.springframework.util.LinkedMultiValueMap;

public class GameLogicNetworkHandler {
    private NetworkManagable manager;

    public GameLogicNetworkHandler(NetworkManagable manager) {
        this.manager = manager;
    }

    @SuppressWarnings("unchecked")
    public ResponseEnvelope<ERequestState> sendHalfMap(String gameID, HalfMap map)
    {
        return manager.postRequest(map, "/" + gameID + "/halfmaps", new LinkedMultiValueMap());
    }

    @SuppressWarnings("unchecked")
    public ResponseEnvelope<GameState> getGameStatus(String gameID, String playerID)
    {
        return manager.getRequest("/" + gameID + "/states/" + playerID, new LinkedMultiValueMap());
    }

    @SuppressWarnings("unchecked")
    public ResponseEnvelope<ERequestState> sendMove(PlayerMove move, String gameID)
    {
        return manager.postRequest(move, "/" + gameID + "/moves" , new LinkedMultiValueMap());
    }
}
