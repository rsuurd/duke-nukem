package duke.level.processors;

import duke.gameplay.active.Notes;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NotesProcessorTest {
    @Test
    void shouldProcess() {
        NotesProcessor processor = new NotesProcessor();

        LevelBuilder builder = mock();

        assertThat(processor.canProcess(NotesProcessor.TILE_ID)).isTrue();

        processor.process(80, NotesProcessor.TILE_ID, builder);

        verify(builder).add(isA(Notes.class));
    }
}
