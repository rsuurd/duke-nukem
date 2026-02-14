package duke.gameplay.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.player.FallHandler.FALL_TICKS;
import static duke.gameplay.player.FallHandler.SLOW_FALL_TICKS;
import static duke.gameplay.player.Player.SPEED;
import static duke.level.Level.TILE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FallHandlerTest {
    private FallHandler handler = new FallHandler();

    @Mock
    private Player player;

    @Test
    void shouldHaveTerminalVelocity() {
        assertEquals(TILE_SIZE, handler.getTerminalVelocity());
    }

    @Test
    void shouldHaveVerticalAcceleration() {
        assertEquals(SPEED, handler.getVerticalAcceleration());
    }

    @Test
    void shouldSlowFall() {
        when(player.getState()).thenReturn(State.FALLING);

        handler.slowFall();

        assertTerminalVelocityForTicks(SLOW_FALL_TICKS);
    }

    @Test
    void shouldFall() {
        when(player.getState()).thenReturn(State.FALLING);

        handler.fall();

        assertTerminalVelocityForTicks(FALL_TICKS);
    }

    private void assertTerminalVelocityForTicks(int t) {
        for (int i = 0; i < t; i++) {
            assertThat(handler.getTerminalVelocity()).isEqualTo(SPEED);

            handler.update(player);
        }

        assertEquals(TILE_SIZE, handler.getTerminalVelocity());
    }
}