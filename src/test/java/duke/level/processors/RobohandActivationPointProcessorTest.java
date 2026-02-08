package duke.level.processors;

import duke.gameplay.active.RobohandActivationPoint;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RobohandActivationPointProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        ActiveProcessor processor = new RobohandActivationPointProcessor();

        assertThat(processor.canProcess(RobohandActivationPointProcessor.TILE_ID)).isTrue();

        processor.process(20, RobohandActivationPointProcessor.TILE_ID, builder);

        verify(builder).add(isA(RobohandActivationPoint.class));
    }
}