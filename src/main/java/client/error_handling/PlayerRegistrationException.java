package client.error_handling;

public class PlayerRegistrationException extends RuntimeException {
    public PlayerRegistrationException(String msg) {
        super(msg);
    }

    public PlayerRegistrationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
