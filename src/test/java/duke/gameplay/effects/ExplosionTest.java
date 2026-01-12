package duke.gameplay.effects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExplosionTest {
    @Test
    void shouldDamage() {
        Explosion explosion = new Explosion(0, 0);

        assertThat(explosion.getDamage()).isGreaterThan(0);
    }
}
