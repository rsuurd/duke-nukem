package duke.dialog;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HintsTest {
    private Hints hints;

    private GameplayContext context;

    @BeforeEach
    void createHints() {
        hints = new Hints();

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldShowHint() {
        assertThat(hints.isEnabled()).isTrue();

        hints.showHint(Hints.Hint.SODA, context);

        verify(context.getDialogManager()).open(any());
    }

    @Test
    void shouldNotShowHintTwice() {
        hints.showHint(Hints.Hint.SODA, context);
        hints.showHint(Hints.Hint.SODA, context);

        verify(context.getDialogManager(), times(1)).open(any());
    }

    @Test
    void shouldToggleHintsOff() {
        hints.toggle();
        assertThat(hints.isEnabled()).isFalse();

        hints.showHint(Hints.Hint.SODA, context);

        verifyNoInteractions(context.getDialogManager());
    }

    @Test
    void shouldToggleHintsOn() {
        hints.toggle();
        assertThat(hints.isEnabled()).isFalse();

        hints.toggle();
        assertThat(hints.isEnabled()).isTrue();
    }

    @Test
    void shouldIndicateHintAvailable() {
        assertThat(hints.isHintAvailable(Hints.Hint.SODA)).isTrue();

        hints.showHint(Hints.Hint.SODA, context);

        assertThat(hints.isHintAvailable(Hints.Hint.SODA)).isFalse();
    }
    
    @Test
    void shouldSetAvailableHints() {
        hints.setAvailableHints(Set.of(Hints.Hint.SODA));

        assertThat(hints.isHintAvailable(Hints.Hint.SODA)).isTrue();
        assertThat(hints.isHintAvailable(Hints.Hint.TURKEY)).isFalse();
    }
}