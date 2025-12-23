package duke.level;

import duke.gameplay.Active;
import duke.level.processors.ActiveProcessor;
import duke.level.processors.ActiveProcessorRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LevelBuilderTest {
    @Mock
    private ActiveProcessorRegistry registry;

    @Test
    void shouldBuildLevel() {
        Level level = new LevelBuilder(registry, 1, new int[Level.WIDTH * Level.HEIGHT]).build();

        assertThat(level).isNotNull();
        assertThat(level.getNumber()).isEqualTo(1);
    }

    @Test
    void shouldProcessActives() {
        int[] data = new int[Level.WIDTH * Level.HEIGHT];
        Arrays.fill(data, 0x3000);
        ActiveProcessor processor = mock(ActiveProcessor.class);
        when(registry.getProcessor(anyInt())).thenReturn(processor);

        LevelBuilder builder = new LevelBuilder(registry, 1, data);
        Level level = builder.build();

        assertThat(level).isNotNull();
        verify(registry, atLeastOnce()).getProcessor(0x3000);
        verify(processor, atLeastOnce()).process(anyInt(), eq(0x3000), same(builder));
    }

    @Test
    void shouldAddActive() {
        Active active = new Active(0, 0, 0, 0) {};
        Level level = new LevelBuilder(registry, 1, new int[Level.WIDTH * Level.HEIGHT]).add(active).build();

        assertThat(level).isNotNull();
        assertThat(level.getActives()).containsExactly(active);
    }
}
