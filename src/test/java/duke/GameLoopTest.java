package duke;

import duke.state.GameState;
import duke.state.StateManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameLoopTest {
    @Mock
    private Renderer renderer;

    @InjectMocks
    private GameContext context;

    @Mock
    private StateManager manager;

    private GameLoop gameLoop;

    @BeforeEach
    void create() {
        gameLoop = new GameLoop(context, manager);
    }

    @Test
    void shouldUpdate() {
        gameLoop.tick();

        verify(manager).update();
    }

    @Test
    void shouldRender() {
        gameLoop.tick();

        verify(renderer).clear();
        verify(manager).render();
        verify(renderer).flip();
    }
}
