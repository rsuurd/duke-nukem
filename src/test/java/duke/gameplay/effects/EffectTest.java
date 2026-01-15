package duke.gameplay.effects;

import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EffectTest {
    @Test
    void shouldRunOnce() {
        AnimationDescriptor animation = mock();
        when(animation.getType()).thenReturn(AnimationDescriptor.Type.ONE_SHOT);

        Effect effect = new Effect(0, 0, animation);

        effect.update(mock());

        assertThat(effect.isDestroyed()).isTrue();
    }

    @Test
    void shouldTrackTtl() {
        Effect effect = new Effect(0, 0, mock(SpriteDescriptor.class), 2);

        effect.update(mock());
        assertThat(effect.isActivated()).isTrue();
        assertThat(effect.isDestroyed()).isFalse();

        effect.update(mock());
        assertThat(effect.isActivated()).isTrue();
        assertThat(effect.isDestroyed()).isTrue();
    }
}
