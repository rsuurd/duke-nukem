package duke.level.processors;

import duke.gameplay.active.Exit;
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
class ExitProcessorTest {
    @Mock
    private LevelBuilder builder;

    private final ExitProcessor processor = new ExitProcessor();

    @Test
    void shouldProcess() {
        assertThat(processor.canProcess(ExitProcessor.EXIT_TILE_ID)).isTrue();

        when(builder.add(any())).thenReturn(builder);
        processor.process(10, ExitProcessor.EXIT_TILE_ID, builder);

        verify(builder).add(isA(Exit.class));
    }
}