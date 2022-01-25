package client.error_handling;

public class GraphShortestPathException extends RuntimeException{

    public GraphShortestPathException(String message) {
        super(message);
    }

    public GraphShortestPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
