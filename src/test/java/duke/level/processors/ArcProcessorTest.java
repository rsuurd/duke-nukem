package duke.level.processors;

import duke.gameplay.active.Arc;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.level.Level.TILE_SIZE;
import static duke.level.LevelBuilder.TOP;
import static duke.level.processors.ArcProcessor.TILE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArcProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Captor
    private ArgumentCaptor<Arc> captor;

    @Test
    void shouldCreateArc() {
        ActiveProcessor processor = new ArcProcessor();

        assertThat(processor.canProcess(TILE_ID)).isTrue();

        when(builder.getTileId(anyInt())).thenReturn(TILE_ID, TILE_ID, TILE_ID, 0);

        processor.process(0, TILE_ID, builder);

        verify(builder, times(3)).replaceTile(anyInt(), eq(TOP));
        verify(builder).add(captor.capture());

        Arc arc = captor.getValue();
        assertThat(arc.getWidth()).isEqualTo(3 * TILE_SIZE);
    }
}
