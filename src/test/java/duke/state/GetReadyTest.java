package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.sfx.Sfx;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

class GetReadyTest {
    @Test
    void shouldShowDialogThenTransitionToGameplay() {
        GetReady getReady = new GetReady();

        GameSystems systems = GameSystemsFixture.create();

        getReady.start(systems);

        verify(systems.getSoundManager()).play(Sfx.START_GAME);
        verify(systems.getDialogManager()).open(isA(Dialog.class));

        for (int i = GetReady.COUNTDOWN; i >= 0; i--) {
            getReady.update(systems);
        }

        verify(systems.getStateRequester()).requestState(isA(GameState.class), eq(StateRequester.Transition.NONE));
    }
}
