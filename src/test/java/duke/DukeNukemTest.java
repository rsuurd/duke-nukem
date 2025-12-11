package duke;

import duke.resources.ResourceLoader;
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
    private GameLoop gameLoop;

    @InjectMocks
    private DukeNukem dukeNukem;

    @Test
    void shouldRunGame() throws InterruptedException {
        dukeNukem.start();
        Thread.sleep(20L);
        dukeNukem.stop();

        verify(resourceLoader).ensureResourcesExist();
        verify(gameLoop, atLeastOnce()).tick();
    }
}
