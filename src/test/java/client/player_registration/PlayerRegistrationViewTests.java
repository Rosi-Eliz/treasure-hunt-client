package client.player_registration;

import client.player_registration.MVC.IPlayerRegistrationController;
import client.player_registration.MVC.IPlayerRegistrationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerRegistrationViewTests {
    private final ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
    private final PrintStream originalOutput = System.out;
    private IPlayerRegistrationController controller;
    private PlayerRegistrationView view;
    private IPlayerRegistrationModel model;

    @BeforeEach
    public void setup() {
        System.setOut(new PrintStream(outputContent));
        controller = Mockito.mock(IPlayerRegistrationController.class);
        model = Mockito.mock(IPlayerRegistrationModel.class);
        view = new PlayerRegistrationView(controller, model);
    }

    @ParameterizedTest
    @CsvSource({"Hello,Hello", "Test,Test"})
    @DisplayName("Test draw")
    public void draw(String testInput, String expectedOutput) {
        view.draw(testInput);
        assertEquals(expectedOutput+"\n", outputContent.toString());
    }
}
