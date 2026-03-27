package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EndTest {
    private End end;

    private GameSystems systems;

    @BeforeEach
    void create() {
        end = new End(0);

        systems = GameSystemsFixture.create();
    }

    @Test
    void shouldShowEndingBackground() {
        end.start(systems);

        verify(systems.getAssets()).getImage("END");
    }

    @Test
    void shouldTransitionToEpilogueWhenFinished() {
        end.start(systems);

        for (int i = 0; i <= End.DURATION; i++) {
            end.update(systems);
        }

        verify(systems.getDialogManager()).open(isA(Dialog.class));

        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        end.update(systems);

        verify(systems.getStateRequester()).requestState(isA(Epilogue.class));
    }
}
