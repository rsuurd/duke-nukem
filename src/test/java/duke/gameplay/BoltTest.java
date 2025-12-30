package duke.gameplay;

import duke.gameplay.effects.Sparks;
import duke.level.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoltTest {
    @Mock
    private Player player;

    @Mock
    private Level level;

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

        bolt.update(new GameplayContext(player, level));

        assertThat(bolt.getX()).isEqualTo(16);
    }

    @Test
    void shouldDespawnWhenFarAway() {
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(player.getX()).thenReturn(200);

        bolt.update(new GameplayContext(player, level));

        assertThat(bolt.isActive()).isFalse();
    }

    @Test
    void shouldCollideWithLevel() {
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        GameplayContext context = spy(new GameplayContext(player, level));

        bolt.update(context);

        assertThat(bolt.isActive()).isFalse();
        verify(context).spawn(any(Sparks.class));
    }
}
