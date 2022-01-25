package client.main;
import client.coordinator.Coordinatable;
import client.coordinator.Coordinator;
import java.util.List;

public class MainClient {
	private static Coordinatable coordinator;

	public static void main(String[] args) {
		String serverBaseUrl = "http://localhost:18235";//args[1];
		String gameId = "22b1f";//args[2];
		List<String> startArguments = List.of(serverBaseUrl, gameId);
		coordinator = new Coordinator();
		coordinator.initiate(startArguments);
	}
}
