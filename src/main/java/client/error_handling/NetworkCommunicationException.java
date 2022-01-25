package client.error_handling;

public class NetworkCommunicationException extends Exception{

    public NetworkCommunicationException(String msg) {
        super(msg);
    }

    public NetworkCommunicationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
