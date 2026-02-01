package duke.gameplay.effects;

import duke.gfx.SpriteDescriptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class BlinkingEffectTest {
    @Test
    void shouldBlink() {
        BlinkingEffect effect = new BlinkingEffect(0, 0, mock(SpriteDescriptor.class), 10);

        for (int i = 0; i < effect.ttl; i++) {
            assertThat(effect.isVisible()).isEqualTo(effect.ttl % 2 == 0);
            effect.update(mock());
        }
    }
}