package duke.level.processors;

import duke.gameplay.active.enemies.FlyingBot;
import duke.gameplay.active.items.Bomb;
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
class BombProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        ActiveProcessor processor = new BombProcessor();
        when(builder.add(any())).thenReturn(builder);

        assertThat(processor.canProcess(BombProcessor.TILE_ID)).isTrue();

        processor.process(20, BombProcessor.TILE_ID, builder);

        verify(builder).add(isA(Bomb.class));
        verify(builder).replaceTile(20, LevelBuilder.TOP);
    }
}
