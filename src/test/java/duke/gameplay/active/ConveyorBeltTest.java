package duke.gameplay.active;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.player.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static duke.level.Level.TILE_SIZE;
import static org.mockito.Mockito.*;

class ConveyorBeltTest {
    private GameplayContext context;

    @BeforeEach
    void create() {
        context = GameplayContextFixture.create();
    }

    @ParameterizedTest
    @EnumSource(Facing.class)
    void shouldPushPlayerOnTop(Facing direction) {
        setupPlayerPosition(32, -32, State.STANDING);
        ConveyorBelt conveyorBelt = new ConveyorBelt(0, 0, 64, direction);

        conveyorBelt.update(context);

        int expectedX = (direction == Facing.RIGHT) ? ConveyorBelt.SPEED : -ConveyorBelt.SPEED;
        verify(context.getPlayer()).addExternalVelocityX(expectedX);
    }

    @Test
    void shouldNotPushPlayerWhenNotOnTop() {
        setupPlayerPosition(128, 0, State.STANDING);
        ConveyorBelt conveyorBelt = new ConveyorBelt(0, 0, 64, Facing.RIGHT);

        conveyorBelt.update(context);

        verify(context.getPlayer(), never()).addExternalVelocityX(anyInt());
    }

    @ParameterizedTest
    @EnumSource(Facing.class)
    void shouldPushClingingPlayer(Facing direction) {
        setupPlayerPosition(32, 16, State.CLINGING);
        ConveyorBelt conveyorBelt = new ConveyorBelt(0, 0, 64, direction);

        conveyorBelt.update(context);

        int expectedX = (direction == Facing.RIGHT) ? -ConveyorBelt.SPEED : ConveyorBelt.SPEED;
        verify(context.getPlayer()).addExternalVelocityX(expectedX);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"CLINGING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotPushWhenPlayerNotClinging(State state) {
        setupPlayerPosition(32, 16, state);
        ConveyorBelt conveyorBelt = new ConveyorBelt(0, 0, 64, Facing.LEFT);

        conveyorBelt.update(context);

        verify(context.getPlayer(), never()).addExternalVelocityX(anyInt());
    }

    private void setupPlayerPosition(int x, int y, State state) {
        when(context.getPlayer().getX()).thenReturn(x);
        when(context.getPlayer().getY()).thenReturn(y);
        when(context.getPlayer().getRow()).thenReturn(y / TILE_SIZE);
        when(context.getPlayer().getCol()).thenReturn(x / TILE_SIZE);
        when(context.getPlayer().getWidth()).thenReturn(TILE_SIZE);
        when(context.getPlayer().getHeight()).thenReturn(2 * TILE_SIZE);
        when(context.getPlayer().getState()).thenReturn(state);
    }
}
