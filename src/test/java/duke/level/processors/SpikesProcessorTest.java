package duke.level.processors;

import duke.gameplay.active.Spikes;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.level.processors.SpikesProcessor.SPIKES_DOWN_TILE_ID;
import static duke.level.processors.SpikesProcessor.SPIKES_UP_TILE_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpikesProcessorTest {
    @Mock
    private LevelBuilder builder;

    private SpikesProcessor processor = new SpikesProcessor();

    @Test
    void shouldCreateSpikesPointingUp() {
        assertTrue(processor.canProcess(SPIKES_UP_TILE_ID));

        when(builder.add(any())).thenReturn(builder);
        when(builder.replaceTile(anyInt(), anyInt())).thenReturn(builder);

        processor.process(20, SPIKES_UP_TILE_ID, builder);

        verify(builder).add(isA(Spikes.class));
        verify(builder).replaceTile(20, LevelBuilder.TOP);
    }

    @Test
    void shouldCreateSpikesPointingDown() {
        assertTrue(processor.canProcess(SPIKES_DOWN_TILE_ID));

        when(builder.add(any())).thenReturn(builder);
        when(builder.replaceTile(anyInt(), anyInt())).thenReturn(builder);

        processor.process(20, SPIKES_DOWN_TILE_ID, builder);

        verify(builder).add(isA(Spikes.class));
        verify(builder).replaceTile(20, LevelBuilder.BOTTOM);
    }
}