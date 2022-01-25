package client.player_registration;

import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import client.network.NetworkManagable;
import org.springframework.util.LinkedMultiValueMap;

public class PlayerRegistrationNetworkHandler {
    private NetworkManagable manager;

    public PlayerRegistrationNetworkHandler(NetworkManagable manager) {
        this.manager = manager;
    }

    public ResponseEnvelope<UniquePlayerIdentifier> sendPlayerRegistrationRequest(String gameID, String firstName, String lastName, String studentID) {
        PlayerRegistration playerReg = new PlayerRegistration(firstName, lastName, studentID);
        return manager.postRequest(playerReg, "/" + gameID + "/players", new LinkedMultiValueMap());
    }
}
