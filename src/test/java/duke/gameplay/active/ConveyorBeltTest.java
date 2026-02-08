package duke.gameplay.active;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static duke.level.Level.TILE_SIZE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConveyorBeltTest {
    private GameplayContext context;

    @BeforeEach
    void create() {
        context = GameplayContextFixture.create();
    }

    @ParameterizedTest
    @EnumSource(Facing.class)
    void shouldPushPlayer(Facing direction) {
        setupPlayerPosition(32, -32);
        ConveyorBelt conveyorBelt = new ConveyorBelt(0, 0, 64, direction);

        conveyorBelt.update(context);

        int expectedX = (direction == Facing.RIGHT) ? 32 + ConveyorBelt.SPEED : 32 - ConveyorBelt.SPEED;
        verify(context.getPlayer()).setX(expectedX);
    }

    private void setupPlayerPosition(int x, int y) {
        when(context.getPlayer().getX()).thenReturn(x);
        when(context.getPlayer().getY()).thenReturn(y);
        when(context.getPlayer().getWidth()).thenReturn(TILE_SIZE);
        when(context.getPlayer().getHeight()).thenReturn(2 * TILE_SIZE);
    }
}
