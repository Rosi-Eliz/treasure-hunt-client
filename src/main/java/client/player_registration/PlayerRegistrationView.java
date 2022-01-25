package client.player_registration;

import client.error_handling.PlayerRegistrationException;
import client.player_registration.MVC.IPlayerRegistrationController;
import client.player_registration.MVC.IPlayerRegistrationModel;
import client.player_registration.MVC.IPlayerRegistrationView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.beans.PropertyChangeListener;

public class PlayerRegistrationView implements IPlayerRegistrationView {
    private IPlayerRegistrationController registrationController;
    private static Logger LOGGER = LoggerFactory.getLogger(PlayerRegistrationView.class);

    private PropertyChangeListener modelChangeListener = event -> {
        Object data = event.getNewValue();
        if (data instanceof PlayerRegistrationResult) {
            PlayerRegistrationResult registration = (PlayerRegistrationResult) data;
            if(registration.getPlayerID() != null) {
                LOGGER.info("Player registered with ID: " + registration.getPlayerID());
                draw("My Player ID:"+ registration.getPlayerID());
                registrationController.didRegisterPlayer(registration.getPlayerID());
            } else if(registration.getErrorMessage() != null ){
                draw("Client error, errormessage: " + registration.getErrorMessage());
                LOGGER.error("Unsuccessful registration: {}", registration.getPlayerID());
            } else {
                draw("Client error, incorrect data processing.");
                LOGGER.error("Incorrect data processing: {}", registration.getPlayerID());
            }
        } else {
            throw new PlayerRegistrationException("Incorrect data processing.");
        }
    };

    public PlayerRegistrationView(IPlayerRegistrationController registrationController,
                                  IPlayerRegistrationModel registrationModel) {
        this.registrationController = registrationController;
        registrationModel.addResultChangeListener(modelChangeListener);
    }

    @Override
    public void draw(String message) {
        System.out.println(message);
    }
}
