package duke.level;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LevelTest {
    @Test
    void shouldRejectIncorrectSize() {
        assertThatThrownBy(() ->
                new Level(0, new int[0], 0)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("Unexpected level size");
    }

    @Test
    void shouldResolveTiles() {
        Level level = new Level(0, new int[Level.WIDTH * 90], 0);

        assertThat(level.getTile(0, 0)).isEqualTo(0);
    }
}