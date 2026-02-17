package duke.level;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LevelDescriptorTest {
    @Test
    void shouldIndicateHallway() {
        assertThat(new LevelDescriptor(1, 0).isHallway()).isFalse();
        assertThat(new LevelDescriptor(2, 0).isHallway()).isTrue();
    }

    @Test
    void shouldCreate() {
        LevelDescriptor descriptor = new LevelDescriptor(1, 2, 3, "Hello, World!");

        assertThat(descriptor.number()).isEqualTo(1);
        assertThat(descriptor.backdrop()).isEqualTo(2);
        assertThat(descriptor.secondaryBackdrop()).isEqualTo(3);
        assertThat(descriptor.message()).isEqualTo("Hello, World!");
    }
}