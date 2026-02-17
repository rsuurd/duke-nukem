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
    private GameSystems systems;

    @Mock
    private StateManager manager;

    private GameLoop gameLoop;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();

        gameLoop = new GameLoop(systems, manager);
    }

    @Test
    void shouldUpdate() {
        gameLoop.tick();

        verify(manager).update();
    }

    @Test
    void shouldRender() {
        gameLoop.tick();

        verify(systems.getRenderer()).clear();
        verify(manager).render();
        verify(systems.getRenderer()).flip();
    }
}
