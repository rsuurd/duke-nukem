package duke.gameplay.active.enemies.behavior;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.DrProton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static duke.gameplay.active.enemies.behavior.DrProtonBehavior.HOVER_TIME;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrProtonBehaviorTest {
    @Mock
    private Random random;

    private EnemyBehavior behavior;

    private GameplayContext context;

    @Mock
    private DrProton boss;

    @BeforeEach
    void create() {
        behavior = new DrProtonBehavior(random);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldFacePlayer() {
        when(context.getPlayer().getX()).thenReturn(32);
        when(boss.getX()).thenReturn(64);

        behavior.behave(context, boss);

        verify(boss).setFacing(Facing.LEFT);
    }

    @Test
    void shouldHover() {
        for (int i = 1; i < HOVER_TIME; i++) {
            behavior.behave(context, boss);
            verify(boss, times(i)).setVelocityX(0);
            verify(boss, times(i)).setVelocityY(0);
        }
    }

    @Test
    void shouldSwoopDownAfterHover() {
        for (int i = 1; i < HOVER_TIME; i++) {
            behavior.behave(context, boss);
        }

        reset(boss);

        when(boss.getFacing()).thenReturn(Facing.RIGHT);
        behavior.behave(context, boss);

        verify(boss).setVelocityX(2);
        verify(boss).setVelocityY(4);
    }

    @Test
    void shouldShootWhileHovering() {
        when(random.nextInt(anyInt())).thenReturn(1);

        new DrProtonBehavior(random).behave(context, boss);

        verify(boss).shoot();
    }

    @Test
    void shouldNotShootIfPlayerFarAway() {
        when(random.nextInt(anyInt())).thenReturn(1);
        when(context.getPlayer().getX()).thenReturn(320);

        new DrProtonBehavior(random).behave(context, boss);

        verify(boss, never()).shoot();
    }
}
