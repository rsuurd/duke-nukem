package duke.gameplay.active.enemies.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.FlyingBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static duke.gameplay.active.enemies.behavior.FlyingBotBehavior.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlyingBotBehaviorTest {
    private GameplayContext context;

    @Mock
    private FlyingBot enemy;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @ParameterizedTest
    @MethodSource("locations")
    void shouldChasePlayer(int turning, int enemyX, int enemyY, int playerX, int playerY, int expectedVelocityX, int expectedVelocityY) {
        EnemyBehavior behavior = new FlyingBotBehavior(turning);

        when(enemy.getX()).thenReturn(enemyX);
        when(enemy.getY()).thenReturn(enemyY);
        when(context.getPlayer().getX()).thenReturn(playerX);
        when(context.getPlayer().getY()).thenReturn(playerY);

        behavior.behave(context, enemy);

        verify(enemy, atLeastOnce()).setVelocityX(expectedVelocityX);
        verify(enemy).setVelocityY(expectedVelocityY);
    }

    static Stream<Arguments> locations() {
        return Stream.of(
                Arguments.of(TURNED_LEFT, 0, 0, 0, 0, 0, 0),
                Arguments.of(TURNED_LEFT, 32, 32, 0, 0, -8, -1),
                Arguments.of(TURNED_RIGHT, 32, 32, 64, 64, 8, 1)
        );
    }

    @Test
    void shouldTurnRight() {
        EnemyBehavior behavior = new FlyingBotBehavior(TURNED_LEFT);

        when(enemy.getX()).thenReturn(32);
        when(context.getPlayer().getX()).thenReturn(64);

        behavior.behave(context, enemy);

        verify(enemy).setRotationFrame(1);
        verify(enemy).setVelocityX(0);
        verify(enemy, never()).shoot();
    }

    @Test
    void shouldTurnLeft() {
        EnemyBehavior behavior = new FlyingBotBehavior(TURNED_RIGHT);

        when(enemy.getX()).thenReturn(64);
        when(context.getPlayer().getX()).thenReturn(32);

        behavior.behave(context, enemy);

        verify(enemy).setRotationFrame(4);
        verify(enemy).setVelocityX(0);
        verify(enemy, never()).shoot();
    }

    @Test
    void shouldShoot() {
        EnemyBehavior behavior = new FlyingBotBehavior(TURNED_LEFT);

        when(enemy.getX()).thenReturn(32);
        when(context.getPlayer().getX()).thenReturn(0);
        behavior.behave(context, enemy);

        verify(enemy).shoot();
    }

    @Test
    void shouldNotShootUntilCooldownIsOver() {
        EnemyBehavior behavior = new FlyingBotBehavior(TURNED_LEFT);

        when(enemy.getX()).thenReturn(32);
        when(context.getPlayer().getX()).thenReturn(0);

        for (int i = 0; i < SHOOT_COOLDOWN; i++) {
            behavior.behave(context, enemy);
            verify(enemy, times(1)).shoot();
        }

        behavior.behave(context, enemy);
        verify(enemy, times(2)).shoot();
    }
}
