package duke.gameplay.active.enemies;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.Health;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.level.Level.TILE_SIZE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnemyTest {
    @Mock
    private EnemyBehavior behavior;

    @Mock
    private Health health;

    private Enemy enemy;

    private GameplayContext context;

    @BeforeEach
    void createEnemy() {
        enemy = new Enemy(0, 0, TILE_SIZE, TILE_SIZE, Facing.LEFT, behavior, health) {
            @Override
            protected void onDestroyed(GameplayContext context) {
            }
        };

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBehave() {
        enemy.update(context);

        verify(behavior).behave(context, enemy);
    }

    @Test
    void shouldTurnAround() {
        enemy.turnAround();
        assertThat(enemy.getFacing()).isEqualTo(Facing.RIGHT);

        enemy.turnAround();
        assertThat(enemy.getFacing()).isEqualTo(Facing.LEFT);
    }

    @Test
    void shouldBeShot() {
        enemy.onShot(context, mock());

        verify(health).takeDamage(1);
    }

    @Test
    void shouldBeDestroyed() {
        when(health.isDead()).thenReturn(true);

        enemy.onShot(context, mock());

        assertThat(enemy.isDestroyed()).isTrue();
    }

    @Test
    void shouldExposeHealth() {
        assertThat(enemy.getHealth()).isSameAs(health);
    }
}
