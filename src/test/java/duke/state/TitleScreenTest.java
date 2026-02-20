package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.menu.Confirmation;
import duke.menu.MainMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_Q;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TitleScreenTest {
    private GameSystems systems;

    private TitleScreen titleScreen;

    @BeforeEach
    void create() {
        systems = spy(GameSystemsFixture.create());

        titleScreen = new TitleScreen();
        titleScreen.start(systems);
    }

    @Test
    void shouldRender() {
        titleScreen.render(systems);

        verify(systems.getAssets()).getImage("DN");
        verify(systems.getRenderer()).draw(any(), eq(0), eq(0));
    }

    @Test
    void shouldOpenMenuOnAnyKey() {
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        titleScreen.update(systems);

        verify(systems.getMenuManager()).open(isA(MainMenu.class), eq(systems));
    }

    @ParameterizedTest
    @ValueSource(ints = {VK_ESCAPE, VK_Q})
    void shouldConfirmQuit(int key) {
        when(systems.getKeyHandler().consume(key)).thenReturn(true);

        titleScreen.update(systems);

        verify(systems.getMenuManager()).open(isA(Confirmation.class), eq(systems));
    }
}
