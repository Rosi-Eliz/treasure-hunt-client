package client.player_registration;
import client.error_handling.NetworkCommunicationException;
import client.player_registration.MVC.IPlayerRegistrationModel;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PlayerRegistrationControllerTests {
    private PlayerRegistrationController controller;
    private IPlayerRegistrationModel model;
    private PlayerRegistrationControllerOutput output;

    @BeforeEach
    public void setup() {
        model = Mockito.mock(IPlayerRegistrationModel.class);
        output = Mockito.mock(PlayerRegistrationControllerOutput.class);
        controller = new PlayerRegistrationController(model, output);
    }

    @Test
    @DisplayName("Test a successful initiate player registration")
    public void testSuccessfulInitiatePlayerRegistration() {
        assertDoesNotThrow(() -> controller.initiatePlayerRegistration());
    }

    @Test
    @DisplayName("Test an unsuccessful player registration")
    public void testUnsuccessfulPlayerRegistration() throws NetworkCommunicationException {
        Mockito.doThrow(new NetworkCommunicationException("Mocked network communication exception"))
                .when(model).requestPlayerRegistration();
        assertDoesNotThrow(() -> controller.initiatePlayerRegistration());
    }

    @Test
    @DisplayName("Test did register player")
    public void testDidRegisterPlayer() {
        controller.didRegisterPlayer("123");
        Mockito.verify(output).didRegisterPlayer("123");
    }
}