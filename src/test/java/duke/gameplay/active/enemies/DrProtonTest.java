package duke.gameplay.active.enemies;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.Health;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DrProtonTest {
    @Mock
    private EnemyBehavior behavior;

    @Mock
    private Health health;

    private DrProton boss;

    private GameplayContext context;

    @BeforeEach
    void create() {
        boss = new DrProton(0, 0, Facing.RIGHT, behavior, health);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBehave() {
        boss.update(context);

        verify(behavior).behave(context, boss);
    }

    @Test
    void shouldShoot() {
        boss.shoot();

        boss.update(context);

        verify(context.getActiveManager()).spawn(isA(EnemyFire.class));
        verify(context.getSoundManager()).play(Sfx.ENEMY_SHOT);
    }

    @Test
    void shouldNotFall() {
        assertThat(boss.getVerticalAcceleration()).isEqualTo(0);
    }
}
