package duke;

import duke.state.StateManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameLoopTest {
    private GameSystems systems;

    @Mock
    private StateManager manager;

    private GameLoop gameLoop;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();
        when(manager.get()).thenReturn(mock());

        gameLoop = new GameLoop(systems, manager);
    }

    @Test
    void shouldUpdate() {
        gameLoop.tick();

        verify(systems.getStateRequester()).update(systems);
        verify(manager.get()).update(systems);
        verify(systems.getPalette()).update();
    }

    @Test
    void shouldRender() {
        gameLoop.tick();

        verify(systems.getRenderer()).clear();
        verify(manager.get()).render(systems);
        verify(systems.getRenderer()).flip();
    }
}
