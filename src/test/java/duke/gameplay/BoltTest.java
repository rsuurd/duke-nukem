package duke.gameplay;

import duke.gameplay.effects.Effect;
import duke.level.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.level.Level.ACTIVE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoltTest {
    @Mock
    private Player player;

    @Mock
    private Level level;

    @Mock
    private ActiveManager activeManager;

    @ParameterizedTest
    @EnumSource(Facing.class)
    void shouldCreate(Facing facing) {
        Player player = new Player(Player.State.STANDING, facing);
        player.setX(192);
        player.setY(192);

        Bolt bolt = Bolt.create(player);

        assertThat(bolt.getX()).isEqualTo(192);
        assertThat(bolt.getY()).isEqualTo(205);
        assertThat(bolt.getVelocityX()).isEqualTo(Bolt.SPEED * ((facing == Facing.RIGHT) ? 1 : -1));
    }

    @Test
    void shouldUpdate() {
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);

        bolt.update(new GameplayContext(player, level, activeManager));

        assertThat(bolt.getX()).isEqualTo(16);
    }

    @Test
    void shouldDespawnWhenFarAway() {
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(player.getX()).thenReturn(200);

        bolt.update(new GameplayContext(player, level, activeManager));

        assertThat(bolt.isActive()).isFalse();
    }

    @Test
    void shouldCollideWithSolids() {
        for (int tileId = Level.SOLIDS; tileId < ACTIVE; tileId++) {
            reset(activeManager);
            Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
            when(level.getTile(anyInt(), anyInt())).thenReturn(tileId);

            GameplayContext context = spy(new GameplayContext(player, level, activeManager));

            bolt.update(context);

            assertThat(bolt.isActive()).isFalse();
            verify(activeManager).spawn(any(Effect.class));
        }
    }

    @Test
    void shouldCollideWithDestructibleBricks() {
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(level.getTile(anyInt(), anyInt())).thenReturn(0x1800);

        bolt.update(new GameplayContext(player, level, activeManager));

        assertThat(bolt.isActive()).isFalse();
        verify(activeManager).spawn(any(Effect.class));
        verify(level).setTile(0, 1, 0x17e0);
    }
}
