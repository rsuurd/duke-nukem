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
        player.setX(16);
        player.setVelocityX(-8);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getX()).isEqualTo(16);
        assertThat(player.getVelocityX()).isEqualTo(0);
        verify(player).onCollision(Collidable.Direction.LEFT);
    }

    @Test
    void shouldCollideRight() {
        player.setX(16);
        player.setVelocityX(8);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getX()).isEqualTo(16);
        assertThat(player.getVelocityX()).isEqualTo(0);
        verify(player).onCollision(Collidable.Direction.RIGHT);
    }

    @Test
    void shouldCollideUp() {
        player.setY(16);
        player.setVelocityY(Player.JUMP_POWER);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getY()).isEqualTo(16);
        assertThat(player.getVelocityY()).isEqualTo(0);
        verify(player).onCollision(Collidable.Direction.UP);
    }

    @Test
    void shouldCollideDown() {
        player.setY(16);
        player.setVelocityY(8);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getY()).isEqualTo(16);
        assertThat(player.getVelocityY()).isEqualTo(0);
        verify(player).onCollision(Collidable.Direction.DOWN);
    }

    @Test
    void shouldApplyGravityWhenJumping() {
        player = spy(new Player(Player.State.JUMPING, Player.Facing.RIGHT));
        player.setVelocityY(-15);
        new Collision().resolve(player, level);

        assertThat(player.getVelocityY()).isEqualTo(-13);
        assertThat(player.getState()).isEqualTo(Player.State.JUMPING);
        verify(player, never()).onCollision(any());
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

        verify(player, never()).onCollision(any());
    }
}
