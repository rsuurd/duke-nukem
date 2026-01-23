package duke.level;

import duke.gameplay.Active;
import duke.level.processors.ActiveProcessor;
import duke.level.processors.ActiveProcessorRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LevelBuilderTest {
    @Mock
    private ActiveProcessorRegistry registry;

    @Mock
    private LevelDescriptor descriptor;

    @Test
    void shouldBuildLevel() {
        Level level = new LevelBuilder(registry, descriptor, new int[Level.WIDTH * Level.HEIGHT]).build();

        assertThat(level).isNotNull();
        assertThat(level.getDescriptor()).isEqualTo(descriptor);
    }

    @Test
    void shouldProcessActives() {
        int[] data = new int[Level.WIDTH * Level.HEIGHT];
        Arrays.fill(data, 0x3000);
        ActiveProcessor processor = mock(ActiveProcessor.class);
        when(registry.getProcessor(anyInt())).thenReturn(processor);

        LevelBuilder builder = new LevelBuilder(registry, descriptor, data);
        Level level = builder.build();

        assertThat(level).isNotNull();
        verify(registry, atLeastOnce()).getProcessor(0x3000);
        verify(processor, atLeastOnce()).process(anyInt(), eq(0x3000), same(builder));
    }

    @Test
    void shouldAddActive() {
        Active active = new Active(0, 0, 0, 0) {
        };
        Level level = new LevelBuilder(registry, descriptor, new int[Level.WIDTH * Level.HEIGHT]).add(active).build();

        assertThat(level).isNotNull();
        assertThat(level.getActives()).containsExactly(active);
    }

    @ParameterizedTest
    @ValueSource(ints = {LevelBuilder.TOP, LevelBuilder.BOTTOM, LevelBuilder.LEFT, LevelBuilder.RIGHT})
    void shouldReplaceTile(int offset) {
        int[] data = new int[Level.WIDTH * Level.HEIGHT];
        int row = 1;
        int col = 1;
        int address = row * Level.WIDTH + col;

        data[address + offset] = 0x1;

        Level level = new LevelBuilder(registry, descriptor, data)
                .replaceTile(address, offset)
                .build();

        assertThat(level.getTile(row, col)).isEqualTo(0x1);
    }

    @Test
    void shouldExposeTileId() {
        int[] data = new int[Level.WIDTH * Level.HEIGHT];
        Arrays.fill(data, 0x23);

        LevelBuilder builder = new LevelBuilder(registry, descriptor, data);

        assertThat(builder.getTileId(10)).isEqualTo(0x23);
    }
}
