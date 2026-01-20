package duke.gameplay.active.enemies;

import duke.gameplay.Collidable;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static duke.gameplay.active.enemies.BouncingMine.BOUNCE_SPEED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class BouncingMineTest {
    private BouncingMine mine;

    private GameplayContext context;

    @BeforeEach
    void createMine() {
        mine = new BouncingMine(0, 0);
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBeDamaging() {
        assertThat(mine.getDamage()).isGreaterThan(0);
    }

    @Test
    void shouldBounce() {
        mine.onCollision(Collidable.Direction.DOWN);

        mine.update(context);

        assertThat(mine.getVelocityY()).isEqualTo(BOUNCE_SPEED);
        verify(context.getSoundManager()).play(Sfx.MINE_BOUNCE);
    }
}