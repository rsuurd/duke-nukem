package duke;

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
    private GameLoop gameLoop;

    @Test
    void shouldRender() {
        gameLoop.tick();

        verify(renderer).clear();
        verify(renderer).flip();
    }
}
