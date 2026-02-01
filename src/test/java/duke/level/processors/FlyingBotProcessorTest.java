package duke.level.processors;

import duke.gameplay.active.enemies.FlyingBot;
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
class FlyingBotProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        ActiveProcessor processor = new FlyingBotProcessor();
        when(builder.add(any())).thenReturn(builder);

        assertThat(processor.canProcess(FlyingBotProcessor.TILE_ID)).isTrue();

        processor.process(20, FlyingBotProcessor.TILE_ID, builder);

        verify(builder).add(isA(FlyingBot.class));
        verify(builder).replaceTile(20, LevelBuilder.LEFT);
    }
}