package duke;

import duke.resources.ResourceLoader;
import duke.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DukeNukemTest {
    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private GameState state;

    @Mock
    private GameLoop gameLoop;

    @InjectMocks
    private GameContext context;

    private DukeNukem dukeNukem;

    @BeforeEach
    void create() {
        dukeNukem = new DukeNukem(context, gameLoop);
    }

    @Test
    void shouldRunGame() throws InterruptedException {
        dukeNukem.start();
        Thread.sleep(20L);
        dukeNukem.stop();

        verify(resourceLoader).ensureResourcesExist();
        verify(state).start(context);
        verify(gameLoop, atLeastOnce()).tick();
    }
}
