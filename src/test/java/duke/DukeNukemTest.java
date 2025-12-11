package duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DukeNukemTest {
    @Mock
    private GameLoop gameLoop;

    private DukeNukem dukeNukem;

    @BeforeEach
    void create() {
        dukeNukem = new DukeNukem(gameLoop);
    }

    @Test
    void shouldRunGame() throws InterruptedException {
        dukeNukem.start();
        Thread.sleep(20L);
        dukeNukem.stop();

        verify(gameLoop, atLeastOnce()).tick();
    }
}
