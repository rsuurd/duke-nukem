package duke.gameplay.active;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SpikesTest {
    @Test
    void shouldDamage() {
        Spikes spikes = new Spikes(0, 0, mock());

        assertThat(spikes.getDamage()).isGreaterThan(0);
    }
}
