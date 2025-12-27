package duke.gfx;

import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class AnimationTest {
    private SpriteDescriptor spriteDescriptor = new SpriteDescriptor(assets -> emptyList(), 20, 0, 0, 2, 2);
    private AnimationDescriptor descriptor = new AnimationDescriptor(spriteDescriptor, 4, 2);

    @Test
    void shouldAdvanceBaseIndex() {
        Animation animation = new Animation(descriptor);

        animation.tick();
        assertThat(animation.getCurrentBaseIndex()).isEqualTo(20);
        animation.tick();
        assertThat(animation.getCurrentBaseIndex()).isEqualTo(24);
    }

    @Test
    void shouldLoop() {
        Animation animation = new Animation(descriptor);

        for (int i = 0; i < descriptor.frames() * descriptor.ticksPerFrame(); i++) {
            animation.tick();
        }

        assertThat(animation.getCurrentBaseIndex()).isEqualTo(20);
    }
}
