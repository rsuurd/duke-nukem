package duke.gameplay.active.enemies.behavior;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.Ed209;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class Ed209BehaviorTest {
    private GameplayContext context;
    private Ed209 enemy;

    @BeforeEach
    void create() {
        context = GameplayContextFixture.create();
        enemy = mock();
    }

    @Test
    void shouldFacePlayer() {
        when(context.getPlayer().getX()).thenReturn(32);
        when(enemy.getX()).thenReturn(64);

        EnemyBehavior behavior = new Ed209Behavior();

        behavior.behave(context, enemy);

        verify(enemy).setFacing(Facing.LEFT);
    }

    @Test
    void shouldShoot() {
        EnemyBehavior behavior = new Ed209Behavior(Ed209Behavior.State.SHOOT, Ed209Behavior.State.IDLE);

        behavior.behave(context, enemy);

        verify(enemy).shoot();
    }

    @Test
    void shouldJump() {
        EnemyBehavior behavior = new Ed209Behavior(Ed209Behavior.State.JUMP, Ed209Behavior.State.IDLE);

        behavior.behave(context, enemy);

        verify(enemy).jump();
    }

    @Test
    void shouldCycleThroughStates() {
        EnemyBehavior behavior = new Ed209Behavior();

        skipIdle(behavior);
        verify(enemy, never()).shoot();
        verify(enemy, never()).jump();

        behavior.behave(context, enemy);
        verify(enemy).shoot();

        skipIdle(behavior);
        behavior.behave(context, enemy);
        verify(enemy).jump();
    }

    private void skipIdle(EnemyBehavior behavior) {
        for (int i = 0; i < Ed209Behavior.IDLE_TICKS; i++) {
            behavior.behave(context, enemy);
        }
    }
}