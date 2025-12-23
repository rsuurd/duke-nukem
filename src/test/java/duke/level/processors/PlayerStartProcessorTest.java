package duke.level.processors;

import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerStartProcessorTest {
    @Mock
    private LevelBuilder builder;

    @Test
    void shouldProcess() {
        when(builder.playerStart(anyInt())).thenReturn(builder);

        new PlayerStartProcessor().process(20, PlayerStartProcessor.TILE_ID, builder);

        verify(builder).playerStart(20);
        verify(builder).replaceTile(20, LevelBuilder.LEFT);
    }
}