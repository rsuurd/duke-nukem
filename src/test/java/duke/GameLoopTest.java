package duke;

import duke.state.GameState;
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

    @Mock
    private GameState gameState;

    @InjectMocks
    private GameContext context;

    private GameLoop gameLoop;

    @BeforeEach
    void create() {
        gameLoop = new GameLoop(context);
    }

    @Test
    void shouldUpdate() {
        gameLoop.tick();

        verify(gameState).update(context);
    }

    @Test
    void shouldRender() {
        gameLoop.tick();

        verify(renderer).clear();
        verify(gameState).render(context);
        verify(renderer).flip();
    }
}
