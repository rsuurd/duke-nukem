package duke.level.processors;

import duke.gameplay.active.Transporter;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransporterProcessorTest {
    @Mock
    private LevelBuilder builder;

    @ParameterizedTest
    @ValueSource(ints = {0x302f, 0x3030})
    void shouldProcess(int tileId) {
        ActiveProcessor processor = new TransporterProcessor();
        when(builder.add(any())).thenReturn(builder);

        assertThat(processor.canProcess(tileId)).isTrue();

        processor.process(20, tileId, builder);

        verify(builder).add(isA(Transporter.class));
        verify(builder).replaceTile(20, LevelBuilder.TOP);
    }
}
