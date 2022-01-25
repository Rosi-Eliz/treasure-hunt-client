package client.player_registration;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import client.error_handling.NetworkCommunicationException;
import client.network.NetworkManagable;
import client.player_registration.MVC.IPlayerRegistrationController;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.util.LinkedMultiValueMap;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

public class PlayerRegistrationModelTests {
    private PlayerRegistrationModel model;
    private PlayerRegistrationControllerOutput output;
    private IPlayerRegistrationController controller;
    private NetworkManagable networkManager;

    @BeforeEach
    void setup() {
        output = Mockito.mock(PlayerRegistrationControllerOutput.class);
        controller = Mockito.mock(IPlayerRegistrationController.class);
        networkManager = Mockito.mock(NetworkManagable.class);
        model = new PlayerRegistrationModel(networkManager,"123");
    }

    @Test
    @DisplayName("Test successful request player registration")
    void testSuccessfulRequestPlayerRegistration() throws NetworkCommunicationException {
        final UniquePlayerIdentifier uniquePlayerIdentifier = new UniquePlayerIdentifier("uuid123");
        final ResponseEnvelope response = new ResponseEnvelope<UniquePlayerIdentifier>(uniquePlayerIdentifier);

        class Holder<T> {
            public T held;
            public void put(T t) {
                held = t;
            }

            public T getHeld() {
                return held;
            }
        }

        Holder<PlayerRegistrationResult> resultHolder = new Holder<>();
        PropertyChangeListener modelChangeListener = event -> {
            Object data = event.getNewValue();
            if (data instanceof PlayerRegistrationResult) {
                resultHolder.put((PlayerRegistrationResult)data);
            } else {
                fail("Incorect changed value type observed");
            }
        };
        model.addResultChangeListener(modelChangeListener);
        Mockito.when(networkManager.postRequest(any(PlayerRegistration.class), anyString(), any(LinkedMultiValueMap.class)))
                .thenReturn(response);

        model.requestPlayerRegistration();
        Mockito.verify(networkManager).postRequest(argThat((PlayerRegistration playerReg) ->
                        playerReg.getStudentFirstName().equals("Rosi-Eliz Veselinova") &&
                playerReg.getStudentLastName().equals("Dzhurkova") &&
                playerReg.getStudentID().equals("11918541")),
                eq("/123/players"),
                argThat((LinkedMultiValueMap multiValueMap) -> multiValueMap.isEmpty()));

        assertEquals(resultHolder.getHeld().getPlayerID(), "uuid123");
        assertEquals(resultHolder.getHeld().getErrorMessage(), null);
    }

    @Test
    @DisplayName("Test unsuccessful request player registration")
    void testUnuccessfulRequestPlayerRegistration() throws NetworkCommunicationException {
        final UniquePlayerIdentifier uniquePlayerIdentifier = new UniquePlayerIdentifier("uuid123");
        final ResponseEnvelope response = new ResponseEnvelope<UniquePlayerIdentifier>("Example exception", "Example exception message");

        class Holder<T> {
            public T held;
            public void put(T t) {
                held = t;
            }

            public T getHeld() {
                return held;
            }
        }

        Holder<PlayerRegistrationResult> resultHolder = new Holder<>();
        PropertyChangeListener modelChangeListener = event -> {
            Object data = event.getNewValue();
            if (data instanceof PlayerRegistrationResult) {
                resultHolder.put((PlayerRegistrationResult)data);
            } else {
                fail("Incorect changed value type observed");
            }
        };
        model.addResultChangeListener(modelChangeListener);
        Mockito.when(networkManager.postRequest(any(PlayerRegistration.class), anyString(), any(LinkedMultiValueMap.class)))
                .thenReturn(response);

        assertThrows(NetworkCommunicationException.class, () -> {
            model.requestPlayerRegistration();
        });

        Mockito.verify(networkManager).postRequest(argThat((PlayerRegistration playerReg) ->
                        playerReg.getStudentFirstName().equals("Rosi-Eliz Veselinova") &&
                                playerReg.getStudentLastName().equals("Dzhurkova") &&
                                playerReg.getStudentID().equals("11918541")),
                eq("/123/players"),
                argThat((LinkedMultiValueMap multiValueMap) -> multiValueMap.isEmpty()));

        assertNull(resultHolder.getHeld().getPlayerID());
        assertEquals(resultHolder.getHeld().getErrorMessage(), "Example exception message Example exception");
    }
}
