package duke.level;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LevelBuilderTest {
    @Test
    void shouldRequireData() {
        assertThatThrownBy(() ->
                new LevelBuilder(0, null).build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRequireDataOfSpecificSize() {
        assertThatThrownBy(() ->
                new LevelBuilder(0, new int[0]).build()
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("Unexpected level size");
    }

    @Test
    void shouldBuildLevel() {
        Level level = new LevelBuilder(1, new int[Level.WIDTH * Level.HEIGHT]).build();

        assertThat(level).isNotNull();
        assertThat(level.getNumber()).isEqualTo(1);
    }
}