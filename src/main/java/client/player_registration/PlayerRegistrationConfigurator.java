package client.player_registration;

import client.network.NetworkManagable;

public class PlayerRegistrationConfigurator {
    private NetworkManagable networkManager;
    private PlayerRegistrationModel model;
    private PlayerRegistrationView view;
    private PlayerRegistrationController controller;
    private PlayerRegistrationControllerOutput output;
    private String gameID;

    public PlayerRegistrationConfigurator(NetworkManagable networkManager,
                                          PlayerRegistrationControllerOutput output,
                                          String gameID) {
        this.networkManager = networkManager;
        this.output = output;
        this.gameID = gameID;
    }

    public void configureAndInitialize()
    {
        model = new PlayerRegistrationModel(networkManager, gameID);
        controller = new PlayerRegistrationController(model, output);
        view = new PlayerRegistrationView(controller, model);
        controller.initiatePlayerRegistration();
    }
}
