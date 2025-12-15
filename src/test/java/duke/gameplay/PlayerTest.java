package duke.gameplay;

import duke.ui.KeyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.Player.WALK_SPEED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    @Mock
    private KeyHandler keyHandler;

    private Player player;

    @BeforeEach
    void create() {
        player = new Player();
    }

    @Test
    void shouldMove() {
        player.moveTo(20, 12);

        assertThat(player.getX()).isEqualTo(20);
        assertThat(player.getY()).isEqualTo(12);
    }

    @Test
    void shouldMoveLeft() {
        when(keyHandler.isLeft()).thenReturn(true);

        player.processInput(keyHandler);

        assertThat(player.getVelocityX()).isEqualTo(-WALK_SPEED);
    }

    @Test
    void shouldMoveRight() {
        when(keyHandler.isRight()).thenReturn(true);

        player.processInput(keyHandler);

        assertThat(player.getVelocityX()).isEqualTo(WALK_SPEED);
    }

    @Test
    void shouldSetVelocity() {
        player.setVelocity(8, -16);

        assertThat(player.getVelocityX()).isEqualTo(8);
        assertThat(player.getVelocityY()).isEqualTo(-16);
    }

    @Test
    void shouldUpdate() {
        player.setVelocity(WALK_SPEED, 0);

        player.update();

        assertThat(player.getX()).isEqualTo(WALK_SPEED);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }
}
