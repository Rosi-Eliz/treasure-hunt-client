package client.error_handling;

public class MapConvertionException extends RuntimeException{

    public MapConvertionException(String message) {
        super(message);
    }

    public MapConvertionException(String message, Throwable cause) {
        super(message, cause);
    }
}
