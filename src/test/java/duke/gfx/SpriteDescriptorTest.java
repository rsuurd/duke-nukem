package duke.gfx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpriteDescriptorTest {
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void shouldRejectInvalidSize(int size) {
        assertThatThrownBy(() ->
                new SpriteDescriptor(assets -> null, 0, 0, 0, size, 1)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("rows must be positive");

        assertThatThrownBy(() ->
                new SpriteDescriptor(assets -> null, 0, 0, 0, 1, size)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("cols must be positive");
    }

    @Test
    void shouldCreateDescriptor() {
        SpriteDescriptor descriptor = new SpriteDescriptor(assets -> null, 0, 0, 0, 2, 2);

        assertThat(descriptor).isNotNull();
    }

    @Test
    void shouldCopyDescriptorWithNewBaseIndex() {
        SpriteDescriptor descriptor = new SpriteDescriptor(assets -> null, 0, 0, 0, 2, 2);
        SpriteDescriptor newDescriptor = descriptor.withBaseIndex(4);

        assertThat(newDescriptor.sheetSelector()).isEqualTo(descriptor.sheetSelector());
        assertThat(newDescriptor.baseIndex()).isEqualTo(4);
        assertThat(newDescriptor.offsetX()).isEqualTo(descriptor.offsetX());
        assertThat(newDescriptor.offsetY()).isEqualTo(descriptor.offsetY());
        assertThat(newDescriptor.rows()).isEqualTo(descriptor.rows());
        assertThat(newDescriptor.cols()).isEqualTo(descriptor.cols());
    }
}
