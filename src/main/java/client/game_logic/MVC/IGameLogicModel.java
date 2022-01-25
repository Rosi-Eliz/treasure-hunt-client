package client.game_logic.MVC;

import client.error_handling.NetworkCommunicationException;

public interface IGameLogicModel {
    void generateAndProcessHalfMap() throws NetworkCommunicationException;
    void startPlaying();
}
