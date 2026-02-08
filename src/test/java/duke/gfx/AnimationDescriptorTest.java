package duke.gfx;

import org.junit.jupiter.api.Test;

import static duke.gfx.SpriteDescriptor.TILES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnimationDescriptorTest {
    private static final SpriteDescriptor SPRITE_DESCRIPTOR = new SpriteDescriptor(TILES, 0, 0, 0, 2, 2);

    @Test
    void shouldCreateAnimationDescriptor() {
        AnimationDescriptor descriptor = new AnimationDescriptor(SPRITE_DESCRIPTOR, 4, 2);

        assertThat(descriptor).isNotNull();
        assertThat(descriptor.getType()).isEqualTo(AnimationDescriptor.Type.LOOP);
        assertThat(descriptor.getDescriptors()).hasSize(4);
        assertThat(descriptor.getDescriptors()).containsExactly(
                SPRITE_DESCRIPTOR.withBaseIndex(0),
                SPRITE_DESCRIPTOR.withBaseIndex(4),
                SPRITE_DESCRIPTOR.withBaseIndex(8),
                SPRITE_DESCRIPTOR.withBaseIndex(12)
        );
    }

    @Test
    void shouldRejectInvalidFrameCount() {
        assertThatThrownBy(() ->
                new AnimationDescriptor(SPRITE_DESCRIPTOR, 0, 0)
        ).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("at least one frame is required");
    }

    @Test
    void shouldRejectInvalidTickCount() {
        assertThatThrownBy(() ->
                new AnimationDescriptor(SPRITE_DESCRIPTOR, 1, 0)
        ).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("ticksPerFrame must be at least 1");
    }

    @Test
    void shouldAdjustTicksPerFrame() {
        AnimationDescriptor descriptor = new AnimationDescriptor(SPRITE_DESCRIPTOR, 4, 2).withTicksPerFrame(5);

        assertThat(descriptor).isNotNull();
        assertThat(descriptor.getFrames()).isEqualTo(4);
        assertThat(descriptor.getTicksPerFrame()).isEqualTo(5);
    }
}
