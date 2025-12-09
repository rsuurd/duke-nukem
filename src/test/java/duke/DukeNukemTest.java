package duke;

import duke.resources.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DukeNukemTest {
    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private GameLoop gameLoop;

    private DukeNukem dukeNukem;

    @BeforeEach
    void create() {
        dukeNukem = new DukeNukem(resourceLoader, gameLoop);
    }

    @Test
    void shouldStart() {
        dukeNukem.start();

        verify(resourceLoader).ensureResourcesExist();
        verify(gameLoop).start();
    }
}
