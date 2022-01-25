package client.player_registration;

public class PlayerRegistrationResult {
    private String playerID;
    private String errorMessage;

    public PlayerRegistrationResult(String playerID, String errorMessage) {
        this.playerID = playerID;
        this.errorMessage = errorMessage;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
