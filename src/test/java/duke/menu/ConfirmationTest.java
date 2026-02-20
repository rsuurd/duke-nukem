package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.awt.event.KeyEvent.VK_Y;
import static org.mockito.Mockito.*;

class ConfirmationTest {
    private GameSystems systems;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();
    }

    @Test
    void shouldPerformActionOnConfirm() {
        Runnable callback = mock();
        Confirmation confirmation = new Confirmation(0, 0, "Y/N?", callback);

        when(systems.getKeyHandler().consume(VK_Y)).thenReturn(true);

        confirmation.update(systems);

        verify(callback).run();
        verify(systems.getMenuManager()).closeAll(systems);
    }

    @Test
    void shouldNotPerformActionWhenNotConfirmed() {
        Runnable callback = mock();
        Confirmation confirmation = new Confirmation(0, 0, "Y/N?", callback);

        when(systems.getKeyHandler().consume(VK_Y)).thenReturn(false);
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        confirmation.update(systems);

        verify(callback, never()).run();
        verify(systems.getMenuManager()).closeAll(systems);
    }
}
