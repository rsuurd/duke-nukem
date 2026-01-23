package duke.gameplay.active;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NeedleTest {
    @Test
    void shouldBeDamaging() {
        Needle needle = new Needle(0, 0);

        assertThat(needle.getDamage()).isGreaterThan(0);
    }
}