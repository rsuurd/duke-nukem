package duke.gameplay.active.items;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BombTest {
    @Test
    void shouldBeDamaging() {
        Bomb bomb = new Bomb(0, 0);

        assertThat(bomb.getDamage()).isGreaterThan(0);
    }
}
