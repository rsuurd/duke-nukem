package duke.gameplay.active.enemies.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.Helicopter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static duke.gameplay.active.enemies.behavior.HelicopterBehavior.*;
import static org.mockito.Mockito.*;

class HelicopterBehaviorTest {
    private HelicopterBehavior behavior;

    private GameplayContext context;

    private Helicopter helicopter;

    @BeforeEach
    void create() {
        behavior = new HelicopterBehavior();

        context = GameplayContextFixture.create();

        helicopter = mock();
        when(helicopter.getHealth()).thenReturn(mock());
    }

    @ParameterizedTest
    @ValueSource(ints = {-64, 0, 64})
    void shouldFlyTowardsPlayer(int playerX) {
        when(context.getPlayer().getX()).thenReturn(playerX);

        behavior.behave(context, helicopter);

        verify(helicopter).setVelocityX(Integer.signum(playerX) * SPEED);
    }

    @Test
    void shouldDescendTowardsPlayer() {
        when(context.getPlayer().getY()).thenReturn(64);

        behavior.behave(context, helicopter);

        verify(helicopter).setVelocityY(DESCEND_SPEED);
    }

    @Test
    void shouldAscendTowardsPlayer() {
        when(helicopter.getY()).thenReturn(64);

        behavior.behave(context, helicopter);

        verify(helicopter).setVelocityY(-SPEED);
    }

    @Test
    void shouldAscendFromGround() {
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(true);

        behavior.behave(context, helicopter);

        verify(helicopter).setVelocityY(-SPEED);
    }

    @Test
    void shouldCrash() {
        when(helicopter.getHealth().isDead()).thenReturn(true);

        behavior.behave(context, helicopter);

        verify(helicopter).setVelocityY(CRASH_SPEED);
    }
}
