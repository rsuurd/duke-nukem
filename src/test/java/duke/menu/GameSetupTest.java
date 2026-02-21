package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameSetupTest {
    private GameSystems systems;
    private GameSetup gameSetup;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();

        gameSetup = new GameSetup(0);
    }

    @Test
    void shouldOpen() {
        gameSetup.open(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldOpenJoystickMode() {
        when(systems.getKeyHandler().consume(VK_J)).thenReturn(true);

        gameSetup.update(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldOpenKeyboardMode() {
        when(systems.getKeyHandler().consume(VK_K)).thenReturn(true);

        gameSetup.update(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }
}
