package duke.gfx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AnimationDescriptorTest {
    @Mock
    private SpriteDescriptor spriteDescriptor;

    @Test
    void shouldCreateAnimationDescriptor() {
        AnimationDescriptor descriptor = new AnimationDescriptor(spriteDescriptor, 4, 2);

        assertThat(descriptor).isNotNull();
    }

    @Test
    void shouldRejectInvalidFrameCount() {
        assertThatThrownBy(() ->
                new AnimationDescriptor(spriteDescriptor, 0, 0)
        ).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("at least one frame is required");
    }

    @Test
    void shouldRejectInvalidTickCount() {
        assertThatThrownBy(() ->
                new AnimationDescriptor(spriteDescriptor, 1, 0)
        ).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("ticksPerFrame must be at least 1");
    }
}
