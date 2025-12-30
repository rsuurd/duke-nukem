package duke.gameplay.effects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SparksTest {
    @Test
    void shouldRunOnce() {
        Sparks sparks = new Sparks(0, 0);

        for (int i = 0; i <= 6; i++) {
            sparks.update(mock());
        }

        assertThat(sparks.getAnimation().isFinished()).isTrue();
    }
}
