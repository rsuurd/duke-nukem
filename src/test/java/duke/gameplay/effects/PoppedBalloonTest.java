package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

class PoppedBalloonTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldSpawnSmokeAfterPop() {
        PoppedBalloon popped = new PoppedBalloon(0, 0);

        popped.update(context);

        assertThat(popped.isDestroyed()).isTrue();
        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }
}