package client.player_registration;

import MessagesBase.ERequestState;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import client.error_handling.NetworkCommunicationException;
import client.network.NetworkManagable;
import client.player_registration.MVC.IPlayerRegistrationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PlayerRegistrationModel implements IPlayerRegistrationModel {
    private PlayerRegistrationNetworkHandler networkHandler;
    private String gameID;
    private PlayerRegistrationResult result;
    private final PropertyChangeSupport resultChanges = new PropertyChangeSupport(this);
    private static Logger LOGGER = LoggerFactory.getLogger(PlayerRegistrationModel.class);
    
    public PlayerRegistrationModel(NetworkManagable networkManager,
                                   String gameID) {
        this.gameID = gameID;
        networkHandler = new PlayerRegistrationNetworkHandler(networkManager);
    }

    @Override
    public void requestPlayerRegistration() throws NetworkCommunicationException {
        ResponseEnvelope<UniquePlayerIdentifier> result = networkHandler.sendPlayerRegistrationRequest(gameID,
                                            "Rosi-Eliz Veselinova",
                                            "Dzhurkova",
                                            "11918541");
        if(result.getState() == ERequestState.Error)
        {
            PlayerRegistrationResult registrationResult = new PlayerRegistrationResult(null,
                    result.getExceptionMessage() + " " + result.getExceptionName());
            setResult(registrationResult);
            throw new NetworkCommunicationException(result.getExceptionMessage());
        } else {
            setResult(new PlayerRegistrationResult(result.getData().get().getUniquePlayerID(), null));
            LOGGER.info("Player with id {} registered successfully!", result.getData().get().getUniquePlayerID());
        }
    }

    public void addResultChangeListener(PropertyChangeListener listener){
        resultChanges.addPropertyChangeListener(listener);
    }

    @Override
    public void setResult(PlayerRegistrationResult result) {
        PlayerRegistrationResult oldResult = this.result;
        this.result = result;
        PlayerRegistrationResult newResult = result;
        resultChanges.firePropertyChange("playerRegistrationResult", oldResult, newResult);
    }
}

