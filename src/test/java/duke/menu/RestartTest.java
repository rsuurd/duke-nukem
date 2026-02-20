package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.awt.event.KeyEvent.VK_Y;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestartTest {
    private GameSystems systems;
    private GameplayContext context;

    private Restart restart;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();
        context = GameplayContextFixture.create();

        restart = new Restart(context);
    }

    @Test
    void shouldOpen() {
        restart.open(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldConfirm() {
        when(systems.getKeyHandler().consume(VK_Y)).thenReturn(true);

        restart.update(systems);

        verify(systems.getMenuManager()).closeAll(systems);
    }

    @Test
    void shouldCloseOnAnyKey() {
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        restart.update(systems);

        verify(systems.getMenuManager()).closeAll(systems);
    }
}
