package duke.level.processors;

import duke.gameplay.active.enemies.DrProton;
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
class DrProtonProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        ActiveProcessor processor = new DrProtonProcessor();

        assertThat(processor.canProcess(DrProtonProcessor.TILE_ID)).isTrue();

        when(builder.add(any())).thenReturn(builder);

        processor.process(0, DrProtonProcessor.TILE_ID, builder);

        verify(builder).add(isA(DrProton.class));
        verify(builder).replaceTile(0, LevelBuilder.LEFT);
    }
}
