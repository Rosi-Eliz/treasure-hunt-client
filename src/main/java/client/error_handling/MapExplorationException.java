package client.error_handling;

public class MapExplorationException extends RuntimeException{

    public MapExplorationException(String msg){
        super(msg);
    }

    public MapExplorationException(String msg, Throwable cause){
        super(msg, cause);
    }
}
