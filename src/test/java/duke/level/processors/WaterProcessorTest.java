package duke.level.processors;

import duke.gameplay.active.Water;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WaterProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        ActiveProcessor processor = new WaterProcessor();

        assertThat(processor.canProcess(WaterProcessor.TILE_ID)).isTrue();

        when(builder.add(any())).thenReturn(builder);

        processor.process(0, WaterProcessor.TILE_ID, builder);

        verify(builder).add(isA(Water.class));
        verify(builder).replaceTile(0, LevelBuilder.LEFT);
    }
}