package duke.gameplay;

import duke.gameplay.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.GameplayContextFixture.SOLID_TILE_FLAG;
import static duke.gameplay.Physics.GRAVITY;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollisionTest {
    @Mock
    private WorldQuery query;

    private Collision collision = new Collision();

    private Player createTestPlayer(int x, int y, int velocityX, int velocityY) {
        Player player = mock();

        when(player.getWidth()).thenReturn(16);
        when(player.getHeight()).thenReturn(32);
        when(player.getX()).thenReturn(x);
        when(player.getY()).thenReturn(y);
        when(player.getVelocityX()).thenReturn(velocityX);
        when(player.getVelocityY()).thenReturn(velocityY);
        when(player.isCollisionEnabled()).thenReturn(true);

        return player;
    }

    @Test
    void shouldCollideLeft() {
        Player player = createTestPlayer(16, 0, -8, 0);
        when(query.isSolid(anyInt(), anyInt())).thenReturn(true);
        when(query.getTileFlags(anyInt(), anyInt())).thenReturn(SOLID_TILE_FLAG);

        collision.resolve(player, query);

        verify(player).setX(16);
        verify(player).onCollision(Collidable.Direction.LEFT, SOLID_TILE_FLAG);
    }

    @Test
    void shouldCollideRight() {
        Player player = createTestPlayer(16, 0, 8, 0);
        when(query.isSolid(anyInt(), anyInt())).thenReturn(true);
        when(query.getTileFlags(anyInt(), anyInt())).thenReturn(SOLID_TILE_FLAG);

        collision.resolve(player, query);

        verify(player).setX(16);
        verify(player).onCollision(Collidable.Direction.RIGHT, SOLID_TILE_FLAG);
    }

    @Test
    void shouldCollideUp() {
        Player player = createTestPlayer(0, 16, 0, -15);
        when(query.isSolid(anyInt(), anyInt())).thenReturn(true);
        when(query.getTileFlags(anyInt(), anyInt())).thenReturn(SOLID_TILE_FLAG);

        collision.resolve(player, query);

        verify(player).setY(16);
        verify(player).onCollision(Collidable.Direction.UP, SOLID_TILE_FLAG);
    }

    @Test
    void shouldCollideDown() {
        Player player = createTestPlayer(0, 16, 0, 8);
        when(query.isSolid(anyInt(), anyInt())).thenReturn(true);
        when(query.getTileFlags(anyInt(), anyInt())).thenReturn(SOLID_TILE_FLAG);

        collision.resolve(player, query);

        verify(player).setY(16);
        // gravity also collides down, should fix this
        verify(player, atLeast(1)).onCollision(Collidable.Direction.DOWN, SOLID_TILE_FLAG);
    }

    @Test
    void shouldApplyGravityWhenJumping() {
        Player player = createTestPlayer(0, 0, 0, -10);
        when(player.getVerticalAcceleration()).thenReturn(GRAVITY);
        when(query.isSolid(anyInt(), anyInt())).thenReturn(false);

        collision.resolve(player, query);

        verify(player).setVelocityY(-8);
        verify(player, never()).onCollision(any(), anyInt());
        verify(query, never()).getTileFlags(anyInt(), anyInt());
    }

    @Test
    void shouldFall() {
        Player player = createTestPlayer(0, 0, 0, 0);
        when(player.getVerticalAcceleration()).thenReturn(8);
        when(player.getTerminalVelocity()).thenReturn(16);
        when(query.isSolid(anyInt(), anyInt())).thenReturn(false);

        collision.resolve(player, query);
        verify(player).setVelocityY(8);
        verify(player).fall();

        verify(player, never()).onCollision(any(), anyInt());
        verify(query, never()).getTileFlags(anyInt(), anyInt());
    }

    @Test
    void shouldNotExceedTerminalVelocity() {
        Player player = createTestPlayer(0, 0, 0, 12);
        when(player.getVerticalAcceleration()).thenReturn(8);
        when(player.getTerminalVelocity()).thenReturn(16);
        when(query.isSolid(anyInt(), anyInt())).thenReturn(false);

        collision.resolve(player, query);
        verify(player).setVelocityY(16);
        verify(player).fall();

        verify(player, never()).onCollision(any(), anyInt());
        verify(query, never()).getTileFlags(anyInt(), anyInt());
    }

    @Test
    void shouldNotCollideWhenDisabled() {
        Physics physics = mock();
        when(physics.isCollisionEnabled()).thenReturn(false);

        collision.resolve(physics, query);

        verify(physics).isCollisionEnabled();
        verifyNoMoreInteractions(physics);
        verifyNoInteractions(query);
    }

    @Test
    void shouldAddExternalVelocity() {
        Player player = createTestPlayer(0, 0, 0, 0);
        when(player.getExternalVelocityX()).thenReturn(8);
        when(player.getExternalVelocityY()).thenReturn(4);

        when(query.isSolid(anyInt(), anyInt())).thenReturn(false);

        collision.resolve(player, query);

        verify(player).setX(8);
        verify(player).setY(4);

        verify(player).resetExternalVelocity();
    }
}
