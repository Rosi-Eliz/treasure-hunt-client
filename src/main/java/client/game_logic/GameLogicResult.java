package client.game_logic;

public class GameLogicResult {
    private String errorMessage;
    private boolean playerHasWon;

    public GameLogicResult(String errorMessage, boolean playerHasWon) {
        this.errorMessage = errorMessage;
        this.playerHasWon = playerHasWon;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMessage()
    {
        return errorMessage != null ? errorMessage : (playerHasWon ? "You won!" : "You lost!");
    }
    public boolean getPlayerHasWon() {
        return playerHasWon;
    }
}
