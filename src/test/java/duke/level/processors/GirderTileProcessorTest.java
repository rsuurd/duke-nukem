package duke.level.processors;

import duke.gameplay.active.Girder;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.active.Girder.GIRDER_BLOCK_TILE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GirderTileProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        ActiveProcessor processor = new GirderTileProcessor();
        when(builder.add(any())).thenReturn(builder);

        assertThat(processor.canProcess(GirderTileProcessor.TILE_ID)).isTrue();

        processor.process(0, GirderTileProcessor.TILE_ID, builder);

        verify(builder).add(isA(Girder.class));
        verify(builder).setTile(0, GIRDER_BLOCK_TILE_ID);
    }
}
