package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ReactorDestructionSequenceTest {
    @Test
    void shouldDestroy() {
        GameplayContext context = GameplayContextFixture.create();
        ReactorDestructionSequence sequence = new ReactorDestructionSequence(0, 0, 48, mock());

        for (int i = 0; i < ReactorDestructionSequence.DESTRUCTION_TIME; i++) {
            assertThat(sequence.isDestroyed()).isFalse();
            sequence.update(context);
        }

        assertThat(sequence.isDestroyed()).isTrue();
        verify(context.getScoreManager(), times(2)).score(eq(10000), anyInt(), anyInt());
    }
}