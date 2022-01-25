package client.error_handling;

public class MapGenerationException extends RuntimeException {

    public MapGenerationException(String msg) {
        super(msg);
    }
    public MapGenerationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
