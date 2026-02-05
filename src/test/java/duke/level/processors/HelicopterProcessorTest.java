package duke.level.processors;

import duke.gameplay.active.enemies.Helicopter;
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
class HelicopterProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        ActiveProcessor processor = new HelicopterProcessor();
        when(builder.add(any())).thenReturn(builder);

        assertThat(processor.canProcess(HelicopterProcessor.TILE_ID)).isTrue();

        processor.process(20, HelicopterProcessor.TILE_ID, builder);

        verify(builder).add(isA(Helicopter.class));
        verify(builder).replaceTile(20, LevelBuilder.LEFT);
    }
}
