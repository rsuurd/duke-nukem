package duke.level.processors;

import duke.gameplay.active.Box;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoxProcessorTest {
    @Mock
    private LevelBuilder builder;

    private BoxProcessor processor = new BoxProcessor();

    @ParameterizedTest
    @MethodSource("tileIds")
    void shouldProcess(int tileId) {
        assertThat(processor.canProcess(tileId)).isTrue();

        when(builder.add(any())).thenReturn(builder);
        when(builder.replaceTile(anyInt(), anyInt())).thenReturn(builder);

        processor.process(20, tileId, builder);

        verify(builder).add(isA(Box.class));
        verify(builder).replaceTile(20, LevelBuilder.LEFT);
    }

    static Stream<Integer> tileIds() {
        return BoxProcessor.BOX_DESCRIPTORS.keySet().stream();
    }
}
