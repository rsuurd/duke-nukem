package duke.gameplay.player;

import duke.gameplay.Facing;
import duke.ui.KeyHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.player.Player.MAX_SPEED;
import static duke.gameplay.player.Player.SPEED;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovementHandlerTest {
    private MovementHandler handler = new MovementHandler();

    @Mock
    private Player player;

    @Mock
    private KeyHandler.Input input;

    @Test
    void shouldNotMoveWhenNoInput() {
        handler.handleInput(player, input);

        verify(player).setVelocityX(0);
        verify(player, never()).walk();
    }

    @Test
    void shouldNotMoveWhenBothLeftAndRightArePressed() {
        when(input.left()).thenReturn(true);
        when(input.right()).thenReturn(true);

        handler.handleInput(player, input);

        verify(player).setVelocityX(0);
        verify(player, never()).walk();
    }

    @ParameterizedTest
    @EnumSource(Facing.class)
    void shouldTurnAround(Facing facing) {
        when(player.getFacing()).thenReturn(facing);

        when(input.left()).thenReturn(facing == Facing.RIGHT);
        when(input.right()).thenReturn(facing == Facing.LEFT);

        handler.handleInput(player, input);

        verify(player).setFacing(facing == Facing.RIGHT ? Facing.LEFT : Facing.RIGHT);
        verify(player).walk();
        verify(player, never()).setVelocityX(anyInt());
    }

    @Test
    void shouldWalkLeft() {
        when(player.getFacing()).thenReturn(Facing.LEFT);

        when(input.left()).thenReturn(true);

        handler.handleInput(player, input);

        verify(player).setVelocityX(-SPEED);
        verify(player).walk();
    }

    @Test
    void shouldWalkRight() {
        when(player.getFacing()).thenReturn(Facing.RIGHT);

        when(input.right()).thenReturn(true);

        handler.handleInput(player, input);

        verify(player).setVelocityX(SPEED);
        verify(player).walk();
    }

    @ParameterizedTest
    @EnumSource(Facing.class)
    void shouldNotExceedMaxSpeed(Facing facing) {
        when(player.getFacing()).thenReturn(facing);
        when(player.getVelocityX()).thenReturn(facing == Facing.LEFT ? -MAX_SPEED : MAX_SPEED);
        when(input.left()).thenReturn(facing == Facing.LEFT);
        when(input.right()).thenReturn(facing == Facing.RIGHT);

        handler.handleInput(player, input);

        verify(player).setVelocityX(facing == Facing.LEFT ? -MAX_SPEED : MAX_SPEED);
    }

    @ParameterizedTest
    @EnumSource(Facing.class)
    void shouldSlowDownToStop(Facing facing) {
        when(player.getState()).thenReturn(State.WALKING);
        when(player.getVelocityX()).thenReturn(facing == Facing.LEFT ? -MAX_SPEED : MAX_SPEED);

        handler.handleInput(player, input);

        verify(player).setVelocityX(0);
        verify(player).stand();
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"WALKING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotStandIfPlayerStopsWhile(State state) {
        when(player.getState()).thenReturn(state);
        when(player.getVelocityX()).thenReturn(MAX_SPEED);

        handler.handleInput(player, input);

        verify(player).setVelocityX(0);
        verify(player, never()).stand();
    }
}