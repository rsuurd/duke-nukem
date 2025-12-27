package duke.level.processors;

import duke.gameplay.active.SecurityCamera;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.level.processors.SecurityCameraProcessor.TILE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityCameraProcessorTest {
    @Mock
    private LevelBuilder builder;

    private SecurityCameraProcessor processor = new SecurityCameraProcessor();

    @Test
    void shouldProcess() {
        assertThat(processor.canProcess(TILE_ID)).isTrue();

        when(builder.add(any())).thenReturn(builder);
        when(builder.replaceTile(anyInt(), anyInt())).thenReturn(builder);

        processor.process(20, TILE_ID, builder);

        verify(builder).add(any(SecurityCamera.class));
        verify(builder).replaceTile(20, LevelBuilder.BOTTOM);
    }
}