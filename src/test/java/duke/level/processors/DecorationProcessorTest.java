package duke.level.processors;

import duke.gameplay.active.Decoration;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DecorationProcessorTest {
    @Mock
    private LevelBuilder builder;

    @ParameterizedTest
    @ValueSource(ints = {0x3025, 0x3026, 0x3027, 0x3028, 0x303d, 0x303e, 0x303f})
    void shouldProcessKnownTileId(int tileId) {
        DecorationProcessor processor = new DecorationProcessor();

        assertThat(processor.canProcess(tileId)).isTrue();

        processor.process(80, tileId, builder);

        verify(builder).add(any(Decoration.class));
    }
}
