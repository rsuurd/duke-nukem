package duke.level.processors;

import duke.gameplay.active.Reactor;
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
class ReactorProcessorTest {
    @Mock
    private LevelBuilder builder;

    private final ReactorProcessor processor = new ReactorProcessor();

    @Test
    void shouldProcess() {
        assertThat(processor.canProcess(ReactorProcessor.TILE_ID)).isTrue();

        when(builder.add(any())).thenReturn(builder);
        processor.process(10, ReactorProcessor.TILE_ID, builder);

        verify(builder).add(isA(Reactor.class));
        verify(builder).replaceTile(10, LevelBuilder.LEFT);
    }
}
