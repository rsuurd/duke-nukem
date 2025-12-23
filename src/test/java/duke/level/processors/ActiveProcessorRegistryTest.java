package duke.level.processors;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ActiveProcessorRegistryTest {
    @Test
    void shouldFindDefaultProcessor() {
        ActiveProcessorRegistry registry = new ActiveProcessorRegistry(Collections.emptyList());

        assertThat(registry.getProcessor(0)).isNotNull();
    }

    @Test
    void shouldFindProcessorForTileId() {
        ActiveProcessor processor = mock(ActiveProcessor.class);
        when(processor.canProcess(anyInt())).thenReturn(true);
        ActiveProcessorRegistry registry = new ActiveProcessorRegistry(List.of(processor));

        assertThat(registry.getProcessor(1)).isSameAs(processor);
    }
}
