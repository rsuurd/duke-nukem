package duke.level.processors;

import duke.gameplay.active.Monitor;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MonitorProcessorTest {
    @Test
    void shouldProcess() {
        ActiveProcessor processor = new MonitorProcessor();

        LevelBuilder builder = mock();
        assertThat(processor.canProcess(MonitorProcessor.TILE_ID)).isTrue();

        processor.process(80, MonitorProcessor.TILE_ID, builder);

        verify(builder).add(isA(Monitor.class));
    }
}