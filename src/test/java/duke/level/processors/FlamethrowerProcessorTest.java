package duke.level.processors;

import duke.gameplay.active.Flamethrower;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
class FlamethrowerProcessorTest {
    @Mock
    private LevelBuilder builder;

    private FlamethrowerProcessor processor = new FlamethrowerProcessor();

    @ParameterizedTest
    @MethodSource("tileIds")
    void shouldProcessRight(int tileId, int offset) {
        when(builder.add(any())).thenReturn(builder);

        assertThat(processor.canProcess(tileId)).isTrue();

        processor.process(80, tileId, builder);

        verify(builder).add(isA(Flamethrower.class));
        verify(builder).replaceTile(80, offset);
    }

    static Stream<Arguments> tileIds() {
        return Stream.of(
                Arguments.of(FlamethrowerProcessor.RIGHT_TILE_ID, LevelBuilder.RIGHT),
                Arguments.of(FlamethrowerProcessor.LEFT_TILE_ID, LevelBuilder.LEFT)
        );
    }
}