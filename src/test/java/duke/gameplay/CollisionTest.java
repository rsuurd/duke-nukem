package duke.gameplay;

import duke.level.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollisionTest {
    @Mock
    private Level level;

    @Spy
    private Player player;

    @Test
    void shouldCollideLeft() {
        player.moveTo(16, 0);
        player.setVelocity(-8, 0);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getX()).isEqualTo(16);
        assertThat(player.getVelocityX()).isEqualTo(0);
        verify(player).onCollide(Collidable.Direction.LEFT);
    }

    @Test
    void shouldCollideRight() {
        player.moveTo(16, 0);
        player.setVelocity(8, 0);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getX()).isEqualTo(16);
        assertThat(player.getVelocityX()).isEqualTo(0);
        verify(player).onCollide(Collidable.Direction.RIGHT);
    }

    @Test
    void shouldCollideUp() {
        player.moveTo(0, 16);
        player.setVelocity(0, -8);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getY()).isEqualTo(16);
        assertThat(player.getVelocityY()).isEqualTo(0);
        verify(player).onCollide(Collidable.Direction.UP);
    }

    @Test
    void shouldCollideDown() {
        player.moveTo(0, 16);
        player.setVelocity(0, 8);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getY()).isEqualTo(16);
        assertThat(player.getVelocityY()).isEqualTo(0);
        verify(player).onCollide(Collidable.Direction.DOWN);
    }

    @Test
    void shouldApplyGravityWhenJumping() {
        player = spy(new Player(Player.State.JUMPING, Player.Facing.RIGHT));
        player.setVelocity(0, -15);
        new Collision().resolve(player, level);

        assertThat(player.getVelocityY()).isEqualTo(-13);
        assertThat(player.getState()).isEqualTo(Player.State.JUMPING);
        verify(player, never()).onCollide(any());
    }

    @Test
    void shouldFall() {
        when(level.isSolid(anyInt(), anyInt())).thenReturn(false);

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

        verify(player, never()).onCollide(any());
    }
}
