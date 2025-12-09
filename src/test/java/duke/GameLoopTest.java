package duke;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameLoopTest {
    @Mock
    private Renderer renderer;

    @InjectMocks
    private GameLoop gameLoop;

    @Test
    void shouldStartRenderAndStop() {
        run(Duration.ofMillis(100));

        verify(renderer).clear();
        verify(renderer).flip();
    }

    private void run(Duration duration) {
        new Thread(() -> {
            try {
                gameLoop.start();
                Thread.sleep(duration.toMillis());
                gameLoop.stop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
