package duke.gameplay.active.enemies;

import duke.gameplay.*;
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
class Ed209Test {
    @Mock
    private EnemyBehavior behavior;

    @Mock
    private Health health;

    private Ed209 ed209;
    private GameplayContext context;

    @BeforeEach
    void createEd209() {
        ed209 = new Ed209(0, 0, Facing.LEFT, behavior, health);
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldShoot() {
        doAnswer(invocation -> {
            ed209.shoot();
            return null;
        }).when(behavior).behave(any(), any());

        ed209.update(context);

        verify(context.getActiveManager()).spawn(isA(EnemyFire.class));
    }

    @Test
    void shouldJump() {
        ed209.jump();

        assertThat(ed209.getVelocityY()).isEqualTo(Ed209.JUMP_STRENGTH);
        assertThat(ed209.getVelocityX()).isEqualTo(-Ed209.SPEED);
        assertThat(ed209.isGrounded()).isFalse();
    }

    @Test
    void shouldLand() {
        ed209.onCollision(Collidable.Direction.DOWN);

        assertThat(ed209.isGrounded()).isTrue();
        assertThat(ed209.getVelocityY()).isEqualTo(0);
        assertThat(ed209.getVelocityX()).isEqualTo(0);
    }

    @Test
    void shouldVisualizeDamage() {
        when(health.getMax()).thenReturn(2);
        when(health.getCurrent()).thenReturn(1);

        for (int i = 0; i < 4; i++) {
            ed209.update(context);
        }

        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }

    @Test
    void shouldBeDestroyed() {
        when(health.isDead()).thenReturn(true);

        ed209.onShot(context, mock());

        assertThat(ed209.isDestroyed()).isTrue();
        verify(context.getScoreManager()).score(2000);
        verify(context.getActiveManager(), times(5)).spawn(any(Effect.class));
        verify(context.getActiveManager(), times(3)).spawn(anyList());
        verify(context.getSoundManager()).play(Sfx.HIT_A_BREAKER);
    }

    @Test
    void shouldBeWokenUp() {
        ed209.wakeUp();

        assertThat(ed209.isAwake()).isTrue();
    }
}