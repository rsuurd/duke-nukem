package duke.level;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LevelDescriptorTest {
    @Test
    void shouldIndicateHallway() {
        assertThat(new LevelDescriptor(1, 0).isHallway()).isFalse();
        assertThat(new LevelDescriptor(2, 0).isHallway()).isTrue();
    }
}