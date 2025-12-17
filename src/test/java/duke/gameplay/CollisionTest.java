package duke.gameplay;

import duke.level.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollisionTest {
    @Mock
    private Level level;

    @Test
    void shouldCollideLeft() {
        Player player = new Player();
        player.moveTo(16, 0);
        player.setVelocity(-8, 0);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getX()).isEqualTo(16);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @Test
    void shouldCollideRight() {
        Player player = new Player();
        player.moveTo(16, 0);
        player.setVelocity(8, 0);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getX()).isEqualTo(16);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @Test
    void shouldCollideUp() {
        Player player = new Player();
        player.moveTo(0, 16);
        player.setVelocity(0, -8);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getY()).isEqualTo(16);
        assertThat(player.getVelocityY()).isEqualTo(0);
    }

    @Test
    void shouldCollideDown() {
        Player player = new Player();
        player.moveTo(0, 16);
        player.setVelocity(0, 8);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getY()).isEqualTo(16);
        assertThat(player.getVelocityY()).isEqualTo(0);
    }

    @Test
    void shouldApplyGravityWhenJumping() {
        Player player = new Player(Player.State.JUMPING, Player.Facing.RIGHT);
        player.setVelocity(0, -15);
        new Collision().resolve(player, level);

        assertThat(player.getVelocityY()).isEqualTo(-13);
        assertThat(player.getState()).isEqualTo(Player.State.JUMPING);
    }

    @Test
    void shouldFall() {
        when(level.isSolid(anyInt(), anyInt())).thenReturn(false);

        Player player = new Player();

        Collision collision = new Collision();

        collision.resolve(player, level);
        assertThat(player.getVelocityY()).isEqualTo(8);
        assertThat(player.getState()).isEqualTo(Player.State.FALLING);

        collision.resolve(player, level);
        assertThat(player.getVelocityY()).isEqualTo(16);
        assertThat(player.getState()).isEqualTo(Player.State.FALLING);

        collision.resolve(player, level);
        assertThat(player.getVelocityY()).isEqualTo(16);
        assertThat(player.getState()).isEqualTo(Player.State.FALLING);
    }

    @Test
    void shouldBumpHead() {
        Player player = spy(new Player(Player.State.JUMPING, Player.Facing.RIGHT));
        player.setVelocity(0, -15);

        when(level.isSolid(anyInt(), anyInt())).thenReturn(true).thenReturn(false);

        new Collision().resolve(player, level);

        verify(player).bump();
        assertThat(player.getVelocityY()).isEqualTo(0);
    }

    @Test
    void shouldLand() {
        Player player = spy(new Player());

        player.fall();
        player.setVelocity(0, 16);

        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        verify(player).land();
        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(Player.State.STANDING);
    }
}
