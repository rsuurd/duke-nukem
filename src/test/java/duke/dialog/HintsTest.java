package duke.dialog;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}