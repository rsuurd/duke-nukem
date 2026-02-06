package duke.level.processors;

import duke.gameplay.active.Bridge;
import duke.level.Level;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.level.processors.BridgeProcessor.TILE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BridgeProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Captor
    private ArgumentCaptor<Bridge> captor;

    private BridgeProcessor processor = new BridgeProcessor();

    @Test
    void shouldProcess() {
        assertThat(processor.canProcess(TILE_ID)).isTrue();
        when(builder.getTileId(anyInt())).thenReturn(TILE_ID, Level.BACKGROUNDS, Level.BACKGROUNDS, Level.BACKGROUNDS, Level.SOLIDS);
        when(builder.add(any())).thenReturn(builder);

        processor.process(20, TILE_ID, builder);

        verify(builder).add(captor.capture());
        verify(builder).replaceTile(20, LevelBuilder.TOP);

        Bridge bridge = captor.getValue();
        assertThat(bridge.getWidth()).isEqualTo(64);
    }
}