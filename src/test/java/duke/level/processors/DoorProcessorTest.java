package duke.level.processors;

import duke.gameplay.active.Door;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static duke.level.Level.TILE_SIZE;
import static duke.level.LevelBuilder.LEFT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoorProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Captor
    private ArgumentCaptor<Door> captor;

    @ParameterizedTest
    @MethodSource("tileIds")
    void shouldProcess(int tileId) {
        ActiveProcessor processor = new DoorProcessor();
        assertThat(processor.canProcess(tileId)).isTrue();

        when(builder.getTileId(anyInt())).thenReturn(tileId, tileId, tileId, tileId, 0);

        processor.process(80, tileId, builder);

        verify(builder, times(4)).replaceTile(anyInt(), eq(LEFT));
        verify(builder).add(captor.capture());

        Door door = captor.getValue();
        assertThat(door.getHeight()).isEqualTo(4 * TILE_SIZE);
    }

    static Stream<Integer> tileIds() {
        return DoorProcessor.DOOR_TILE_IDS.keySet().stream();
    }
}
