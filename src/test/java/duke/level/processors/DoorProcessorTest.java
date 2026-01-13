package duke.level.processors;

import duke.gameplay.active.Door;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoorProcessorTest {
    @Mock
    private LevelBuilder builder;

    private ActiveProcessor processor = new DoorProcessor();

    @ParameterizedTest
    @MethodSource("tileIds")
    void shouldProcess(int tileId) {
        when(builder.add(any())).thenReturn(builder);

        assertThat(processor.canProcess(tileId)).isTrue();

        processor.process(80, tileId, builder);

        verify(builder).add(isA(Door.class));
        verify(builder).replaceTile(80, LevelBuilder.LEFT);
    }

    static Stream<Integer> tileIds() {
        return DoorProcessor.DOOR_TILE_IDS.keySet().stream();
    }
}
