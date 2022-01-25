package client.player_registration;

import client.error_handling.NetworkCommunicationException;
import client.player_registration.MVC.IPlayerRegistrationController;
import client.player_registration.MVC.IPlayerRegistrationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerRegistrationController implements IPlayerRegistrationController {
    private IPlayerRegistrationModel registrationModel;
    private PlayerRegistrationControllerOutput output;
    private static Logger LOGGER = LoggerFactory.getLogger(PlayerRegistrationController.class);

    public PlayerRegistrationController(IPlayerRegistrationModel registrationModel,
                                        PlayerRegistrationControllerOutput output) {
        this.registrationModel = registrationModel;
        this.output = output;
    }

    @Override
    public void initiatePlayerRegistration() {
        try {
            registrationModel.requestPlayerRegistration();
        } catch (NetworkCommunicationException e) {
            LOGGER.error("Error while requesting player registration: {}", e.getMessage());
        }
    }

    public void didRegisterPlayer(String playerID) {
        output.didRegisterPlayer(playerID);
    }
}
