package duke.level.processors;

import duke.gameplay.Facing;
import duke.gameplay.active.ConveyorBelt;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static duke.gameplay.active.ConveyorBelt.SOLID_LEFT_CONVEYOR_TILE_ID;
import static duke.gameplay.active.ConveyorBelt.SOLID_RIGHT_CONVEYOR_TILE_ID;
import static duke.level.Level.TILE_SIZE;
import static duke.level.processors.ConveyorBeltProcessor.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConveyorBeltProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Captor
    private ArgumentCaptor<ConveyorBelt> captor;

    @ParameterizedTest
    @MethodSource("tileIds")
    void shouldProcess(int tileId, List<Integer> nextTiles, Facing direction) {
        ActiveProcessor processor = new ConveyorBeltProcessor();

        Iterator<Integer> iterator = nextTiles.iterator();
        doAnswer(invocationOnMock -> iterator.next()).when(builder).getTileId(anyInt());
        when(builder.setTile(anyInt(), anyInt())).thenReturn(builder);

        processor.process(0, tileId, builder);

        verify(builder).setTile(0, SOLID_LEFT_CONVEYOR_TILE_ID);
        verify(builder).setTile(nextTiles.size(), SOLID_RIGHT_CONVEYOR_TILE_ID);
        verify(builder).add(captor.capture());

        ConveyorBelt conveyorBelt = captor.getValue();
        assertThat(conveyorBelt.getWidth()).isEqualTo((nextTiles.size() + 1) * TILE_SIZE);
        assertThat(conveyorBelt.getDirection()).isEqualTo(direction);
    }

    static Stream<Arguments> tileIds() {
        return Stream.of(
                Arguments.of(LEFT_START_TILE_ID, List.of(0x0, 0x0, LEFT_END_TILE_ID), Facing.LEFT),
                Arguments.of(RIGHT_START_TILE_ID, List.of(RIGHT_END_TILE_ID), Facing.RIGHT)
        );
    }
}
