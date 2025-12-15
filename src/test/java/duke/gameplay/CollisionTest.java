package duke.gameplay;

import duke.level.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollisionTest {
    private Player player = new Player();

    @Mock
    private Level level;

    @Test
    void shouldCollideLeft() {
        player.moveTo(16, 0);
        player.setVelocity(-8, 0);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getX()).isEqualTo(16);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @Test
    void shouldCollideRight() {
        player.moveTo(16, 0);
        player.setVelocity(8, 0);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getX()).isEqualTo(16);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @Test
    void shouldCollideUp() {
        player.moveTo(0, 16);
        player.setVelocity(0, -8);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getY()).isEqualTo(16);
        assertThat(player.getVelocityY()).isEqualTo(0);
    }

    @Test
    void shouldCollideDown() {
        player.moveTo(0, 16);
        player.setVelocity(0, 8);
        when(level.isSolid(anyInt(), anyInt())).thenReturn(true);

        new Collision().resolve(player, level);

        assertThat(player.getY()).isEqualTo(16);
        assertThat(player.getVelocityY()).isEqualTo(0);
    }
}