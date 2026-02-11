package duke.gfx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class AnimationTest {
    @Test
    void shouldAdvanceFrame() {
        Animation animation = new Animation(ANIMATION_DESCRIPTOR);

        for (int i = 0; i < TICKS_PER_FRAME; i++) {
            assertThat(animation.getCurrentFrameIndex()).isEqualTo(0);
            animation.tick();
        }

        animation.tick();
        assertThat(animation.getCurrentFrameIndex()).isEqualTo(1);
        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX + FRAME_OFFSET);
    }

    @Test
    void shouldLoopForward() {
        Animation animation = new Animation(ANIMATION_DESCRIPTOR);

        for (int i = 0; i < FRAMES * TICKS_PER_FRAME; i++) {
            animation.tick();
        }

        animation.tick();

        assertThat(animation.getCurrentFrameIndex()).isEqualTo(0);
        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX);
    }

    @Test
    void shouldLoopBackward() {
        Animation animation = new Animation(ANIMATION_DESCRIPTOR);
        animation.reverse();

        animation.tick();

        assertThat(animation.getCurrentFrameIndex()).isEqualTo(LAST_FRAME);
        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(LAST_FRAME_BASE_INDEX);
    }

    @Test
    void shouldRunOnce() {
        Animation animation = new Animation(new AnimationDescriptor(SPRITE_DESCRIPTOR, FRAMES, TICKS_PER_FRAME, AnimationDescriptor.Type.ONE_SHOT));

        for (int i = 0; i < FRAMES * TICKS_PER_FRAME; i++) {
            animation.tick();
        }

        assertThat(animation.getCurrentFrameIndex()).isEqualTo(LAST_FRAME);
        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(LAST_FRAME_BASE_INDEX);
        assertThat(animation.isFinished()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("oneShotAnimations")
    void shouldRunOneShotAnimation(Animation animation, int expectedFrameIndex, int expectedBaseIndex) {
        for (int i = 0; i < FRAMES * TICKS_PER_FRAME; i++) {
            animation.tick();
        }

        assertThat(animation.getCurrentFrameIndex()).isEqualTo(expectedFrameIndex);
        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(expectedBaseIndex);
        assertThat(animation.isFinished()).isTrue();
    }

    static Stream<Arguments> oneShotAnimations() {
        Animation forward = new Animation(new AnimationDescriptor(SPRITE_DESCRIPTOR, FRAMES, TICKS_PER_FRAME, AnimationDescriptor.Type.ONE_SHOT));
        Animation reverse = new Animation(new AnimationDescriptor(SPRITE_DESCRIPTOR, FRAMES, TICKS_PER_FRAME, AnimationDescriptor.Type.ONE_SHOT));
        reverse.reverse();
        reverse.reset();

        return Stream.of(
                Arguments.of(forward, LAST_FRAME, LAST_FRAME_BASE_INDEX),
                Arguments.of(reverse, 0, BASE_INDEX)
        );
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
    void settingSameAnimationShouldDoNothing() {
        Animation animation = new Animation(ANIMATION_DESCRIPTOR);
        animation.tick();
        animation.tick();
        animation.tick();
        animation.tick();

        animation.setAnimation(ANIMATION_DESCRIPTOR);

        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX + FRAME_OFFSET);
    }

    @Test
    void shouldReverseAnimation() {
        Animation animation = new Animation(ANIMATION_DESCRIPTOR);
        animation.reverse();
        animation.reset();
        assertThat(animation.isReset()).isTrue();
        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(LAST_FRAME_BASE_INDEX);

        for (int i = 0; i < TICKS_PER_FRAME; i++) {
            animation.tick();
        }

        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(LAST_FRAME_BASE_INDEX - FRAME_OFFSET);
    }

    @Test
    void shouldResetAnimationToStart() {
        Animation animation = new Animation(new AnimationDescriptor(SPRITE_DESCRIPTOR, 4, 1));

        animation.tick();
        animation.reset();

        assertThat(animation.isReset()).isTrue();
        assertThat(animation.getSpriteDescriptor().baseIndex()).isEqualTo(BASE_INDEX);
    }

    private static final int BASE_INDEX = 20;
    private static final SpriteDescriptor SPRITE_DESCRIPTOR = new SpriteDescriptor(assets -> emptyList(), BASE_INDEX, 0, 0, 2, 2);
    private static final int FRAME_OFFSET = SPRITE_DESCRIPTOR.rows() * SPRITE_DESCRIPTOR.cols();
    private static final int FRAMES = 4;
    private static final int TICKS_PER_FRAME = 4;
    private static final int LAST_FRAME = FRAMES - 1;
    private static final int LAST_FRAME_BASE_INDEX = BASE_INDEX + LAST_FRAME * FRAME_OFFSET;

    private static final AnimationDescriptor ANIMATION_DESCRIPTOR = new AnimationDescriptor(SPRITE_DESCRIPTOR, FRAMES, TICKS_PER_FRAME);
}
