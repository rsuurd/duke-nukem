package duke.level;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LevelDataTest {
    @Test
    void shouldRejectIncorrectSize() {
        assertThatThrownBy(() ->
                new LevelData(new int[0])
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("Unexpected level size");
    }

    @Test
    void shouldResolveTiles() {
        LevelData data = new LevelData(new int[128 * 90]);

        assertThat(data.getTile(0, 0)).isEqualTo(0);
    }
}