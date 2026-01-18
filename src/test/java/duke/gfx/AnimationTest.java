package duke.gfx;

import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class AnimationTest {
    @Test
    void shouldAdvanceBaseIndex() {
        Animation animation = new Animation(ANIMATION_DESCRIPTOR);

        for (int i = 0; i < TICKS_PER_FRAME; i++) {
            assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX);
            animation.tick();
        }

        animation.tick();
        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(NEXT_FRAME_INDEX);
    }

    @Test
    void shouldLoop() {
        Animation animation = new Animation(ANIMATION_DESCRIPTOR);

        for (int i = 0; i < FRAMES * TICKS_PER_FRAME; i++) {
            animation.tick();
        }

        animation.tick();

        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX);
    }

    @Test
    void shouldRunOnce() {
        Animation animation = new Animation(new AnimationDescriptor(SPRITE_DESCRIPTOR, FRAMES, TICKS_PER_FRAME, AnimationDescriptor.Type.ONE_SHOT));

        for (int i = 0; i < FRAMES * TICKS_PER_FRAME; i++) {
            animation.tick();
        }

        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX + 12);
        assertThat(animation.isFinished()).isTrue();
    }

    @Test
    void shouldSwitchAnimation() {
        AnimationDescriptor different = new AnimationDescriptor(SPRITE_DESCRIPTOR, 2, 2);

        Animation animation = new Animation(ANIMATION_DESCRIPTOR);
        animation.tick();
        animation.tick();
        animation.tick();

        animation.setAnimation(different);
        animation.tick();

        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX);
    }

    @Test
    void shouldNotSwitchToSameAnimation() {
        Animation animation = new Animation(ANIMATION_DESCRIPTOR);
        animation.tick();
        animation.tick();
        animation.tick();

        animation.setAnimation(ANIMATION_DESCRIPTOR);

        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(NEXT_FRAME_INDEX);
    }

    @Test
    void shouldReverseAnimation() {
        Animation animation = new Animation(ANIMATION_DESCRIPTOR);
        animation.reverse();
        animation.reset();
        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX + 12);

        for (int i = 0; i < TICKS_PER_FRAME; i++) {
            animation.tick();
        }

        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX + 8);
    }

    private static final int BASE_INDEX = 20;
    private static final int NEXT_FRAME_INDEX = BASE_INDEX + 4;
    private static final SpriteDescriptor SPRITE_DESCRIPTOR = new SpriteDescriptor(assets -> emptyList(), BASE_INDEX, 0, 0, 2, 2);
    private static final int FRAMES = 4;
    private static final int TICKS_PER_FRAME = 2;
    private static final AnimationDescriptor ANIMATION_DESCRIPTOR = new AnimationDescriptor(SPRITE_DESCRIPTOR, FRAMES, TICKS_PER_FRAME);
}
