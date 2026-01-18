package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TechbotDestructionTest {
    private TechbotDestruction destruction;

    private GameplayContext context;

    @BeforeEach
    void createDestruction() {
        destruction = new TechbotDestruction(0, 0);
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldRunDestructionSequence() {
        while (!destruction.isDestroyed()) {
            destruction.update(context);
        }

        verify(context.getSoundManager(), times(1)).play(Sfx.SMALL_DEATH);
        verify(context.getActiveManager(), times(1)).spawn(isA(Effect.class));
        verify(context.getScoreManager()).score(100);
    }
}