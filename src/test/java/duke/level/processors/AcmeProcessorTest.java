package duke.level.processors;

import duke.gameplay.active.Acme;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.level.processors.AcmeProcessor.TILE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcmeProcessorTest {
    @Mock
    private LevelBuilder builder;

    private AcmeProcessor processor = new AcmeProcessor();

    @Test
    void shouldProcess() {
        assertThat(processor.canProcess(TILE_ID)).isTrue();

        when(builder.add(any())).thenReturn(builder);
        when(builder.replaceTile(anyInt(), anyInt())).thenReturn(builder);

        processor.process(20, TILE_ID, builder);

        verify(builder).add(any(Acme.class));
        verify(builder).replaceTile(20, LevelBuilder.LEFT);
    }
}
