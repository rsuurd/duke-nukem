package duke.level.processors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ActiveProcessorRegistryTest {
    private ActiveProcessorRegistry registry;

    @BeforeEach
    void createRegistry() {
        registry = new ActiveProcessorRegistry();
    }

    @Test
    void shouldFindDefaultProcessor() {
        assertThat(registry.getProcessor(0)).isNotNull();
    }

    @Test
    void shouldFindProcessorForTileId() {
        ActiveProcessor processor = mock(ActiveProcessor.class);
        registry.addProcessor(1, processor);

        assertThat(registry.getProcessor(1)).isSameAs(processor);
    }
}
