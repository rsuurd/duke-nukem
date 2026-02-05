package duke.gameplay.active.enemies;

import duke.gameplay.Collidable;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelicopterTest {
    private Helicopter helicopter;

    @Mock
    private EnemyBehavior behavior;

    @Mock
    private Health health;

    private GameplayContext context;

    @BeforeEach
    void create() {
        helicopter = new Helicopter(0, 0, behavior, health);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldFly() {
        helicopter.update(context);

        verify(behavior).behave(context, helicopter);
        verify(context.getSoundManager()).play(Sfx.WALKING);
    }

    @Test
    void shouldHover() {
        assertThat(helicopter.getVerticalAcceleration()).isEqualTo(0);
    }

    @Test
    void shouldNotBeDestroyedUntilCrashed() {
        helicopter.destroy();
        helicopter.onDestroyed(context);

        assertThat(helicopter.isDestroyed()).isFalse();
        verifyNoInteractions(context.getActiveManager(), context.getScoreManager(), context.getSoundManager());
    }

    @Test
    void shouldNotCrashIfNotDead() {
        helicopter.onCollision(Collidable.Direction.DOWN);

        helicopter.update(context);

        assertThat(helicopter.isDestroyed()).isFalse();
    }

    @Test
    void shouldCrashIfDead() {
        when(health.isDead()).thenReturn(true);
        helicopter.onCollision(Collidable.Direction.DOWN);

        helicopter.update(context);

        assertThat(helicopter.isDestroyed()).isTrue();
        verify(context.getActiveManager(), times(2)).spawn(isA(Effect.class));
        verify(context.getActiveManager(), times(5)).spawn(anyList());
        verify(context.getSoundManager()).play(Sfx.BOX_EXPLODE);
        verify(context.getScoreManager()).score(5000);
    }
}
