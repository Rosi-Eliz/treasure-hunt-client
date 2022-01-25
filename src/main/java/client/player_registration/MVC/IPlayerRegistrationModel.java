package client.player_registration.MVC;

import client.error_handling.NetworkCommunicationException;
import client.player_registration.PlayerRegistrationResult;

import java.beans.PropertyChangeListener;

public interface IPlayerRegistrationModel {
    void requestPlayerRegistration() throws NetworkCommunicationException;
    void setResult(PlayerRegistrationResult result);
    void addResultChangeListener(PropertyChangeListener listener);
}
