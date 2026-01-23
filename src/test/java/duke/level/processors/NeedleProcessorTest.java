package duke.level.processors;

import duke.gameplay.active.Needle;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class NeedleProcessorTest {
    @Test
    void shouldProcess() {
        ActiveProcessor processor = new NeedleProcessor();

        LevelBuilder builder = mock();
        when(builder.add(any())).thenReturn(builder);

        assertThat(processor.canProcess(NeedleProcessor.TILE_ID)).isTrue();

        processor.process(80, NeedleProcessor.TILE_ID, builder);

        verify(builder).add(isA(Needle.class));
        verify(builder).replaceTile(80, LevelBuilder.TOP);
    }
}
