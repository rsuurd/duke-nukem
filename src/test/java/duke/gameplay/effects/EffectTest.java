package duke.gameplay.effects;

import duke.gfx.AnimationDescriptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EffectTest {
    @Test
    void shouldRunOnce() {
        AnimationDescriptor animation = mock();
        when(animation.getType()).thenReturn(AnimationDescriptor.Type.ONE_SHOT);

        Effect effect = new Effect(0, 0, animation);

        effect.update(mock());

        assertThat(effect.isActive()).isFalse();
    }
}
