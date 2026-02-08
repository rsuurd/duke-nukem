package duke.level.processors;

import duke.gameplay.active.Missile;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MissileProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        when(builder.add(any())).thenReturn(builder);

        ActiveProcessor processor = new MissileProcessor();

        assertThat(processor.canProcess(MissileProcessor.TILE_ID)).isTrue();

        processor.process(0, MissileProcessor.TILE_ID, builder);

        verify(builder).add(any(Missile.class));
        verify(builder).replaceTile(0, LevelBuilder.TOP);
    }
}
