package duke;

import duke.state.StateManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameLoopTest {
    private GameContext context;

    @Mock
    private StateManager manager;

    private GameLoop gameLoop;

    @BeforeEach
    void create() {
        context = GameContextFixture.create();

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

        verify(context.getRenderer()).clear();
        verify(manager).render();
        verify(context.getRenderer()).flip();
    }
}
