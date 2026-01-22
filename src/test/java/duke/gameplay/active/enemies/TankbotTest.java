package duke.gameplay.active.enemies;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.Health;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.effects.Effect;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TankbotTest {
    @Mock
    private EnemyBehavior behavior;

    @Mock
    private Health health;

    private Tankbot tankbot;

    private GameplayContext context;

    @BeforeEach
    void createTankbot() {
        tankbot = new Tankbot(0, 0, Facing.RIGHT, behavior, health);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBeDamaging() {
        assertThat(tankbot.getDamage()).isGreaterThan(0);
    }

    @Test
    void shouldShootWhenTurnedAroundAndPlayerClose() {
        doAnswer(invocation -> {
            tankbot.turnAround();

            return null;
        }).when(behavior).behave(context, tankbot);

        tankbot.update(context);

        verify(context.getActiveManager()).spawn(isA(EnemyFire.class));
        verify(context.getSoundManager()).play(Sfx.ENEMY_SHOT);
    }

    @Test
    void shouldNotShootWhenTurnedAroundAndPlayerFarAway() {
        doAnswer(invocation -> {
            tankbot.turnAround();

            return null;
        }).when(behavior).behave(context, tankbot);
        when(context.getPlayer().getX()).thenReturn(1024);

        tankbot.update(context);

        verify(context.getActiveManager(), never()).spawn(any(EnemyFire.class));
    }

    @Test
    void shouldNotShootWhenNotTurnedAround() {
        tankbot.update(context);

        verify(context.getActiveManager(), never()).spawn(any(EnemyFire.class));
    }

    @Test
    void shouldVisualizeDamage() {
        when(health.getMax()).thenReturn(2);
        when(health.getCurrent()).thenReturn(1);

        for (int i = 0; i < 6; i++) {
            tankbot.update(context);
        }

        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }

    @Test
    void shouldBeDestroyed() {
        when(health.isDead()).thenReturn(true);

        tankbot.onShot(context, mock());

        assertThat(tankbot.isDestroyed()).isTrue();
        verify(context.getScoreManager()).score(2500);
        verify(context.getActiveManager()).spawn(any(Effect.class));
        verify(context.getSoundManager()).play(Sfx.SMALL_DEATH);
    }
}
