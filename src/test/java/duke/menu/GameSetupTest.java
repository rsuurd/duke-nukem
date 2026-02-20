package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.awt.event.KeyEvent.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class GameSetupTest {
    private GameSystems systems;
    private GameplayContext context;
    private GameSetup gameSetup;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();
        context = GameplayContextFixture.create();

        gameSetup = new GameSetup(context);
    }

    @Test
    void shouldOpen() {
        gameSetup.open(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldOpenJoystickMode() {
        when(systems.getKeyHandler().getPressedKey()).thenReturn(VK_J);

        gameSetup.update(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldOpenKeyboardMode() {
        when(systems.getKeyHandler().getPressedKey()).thenReturn(VK_K);

        gameSetup.update(systems);

        verify(systems.getDialogManager(), never()).open(isA(Dialog.class));
    }

    @Test
    void shouldToggleSound() {
        when(systems.getKeyHandler().getPressedKey()).thenReturn(VK_S);

        gameSetup.update(systems);

        verify(context.getSoundManager()).toggle();
        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldToggleHints() {
        when(systems.getKeyHandler().getPressedKey()).thenReturn(VK_H);

        gameSetup.update(systems);

        verify(context.getHints()).toggle();
        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }
}
