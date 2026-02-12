package duke.gameplay.active.enemies;

import duke.gameplay.Collidable;
import duke.gameplay.Facing;
import org.junit.jupiter.api.Test;

import static duke.gameplay.GameplayContextFixture.SOLID_TILE_FLAG;
import static org.assertj.core.api.Assertions.assertThat;

class EnemyFireTest {
    @Test
    void shouldBeDamaging() {
        EnemyFire enemyFire = new EnemyFire(0, 0, Facing.LEFT);
        assertThat(enemyFire.getDamage()).isGreaterThan(0);
    }

    @Test
    void shouldBeDestroyedOnCollision() {
        EnemyFire enemyFire = new EnemyFire(0, 0, Facing.LEFT);
        assertThat(enemyFire.isDestroyed()).isFalse();

        enemyFire.onCollision(Collidable.Direction.LEFT, SOLID_TILE_FLAG);
        assertThat(enemyFire.isDestroyed()).isTrue();
    }
}