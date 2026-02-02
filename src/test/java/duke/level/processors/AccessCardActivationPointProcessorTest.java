package duke.level.processors;

import duke.gameplay.active.AccessCardActivationPoint;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccessCardActivationPointProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        ActiveProcessor processor = new AccessCardActivationPointProcessor();

        assertThat(processor.canProcess(AccessCardActivationPointProcessor.TILE_ID)).isTrue();

        processor.process(20, AccessCardActivationPointProcessor.TILE_ID, builder);

        verify(builder).add(isA(AccessCardActivationPoint.class));
    }
}