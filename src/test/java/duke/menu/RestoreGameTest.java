package duke.menu;

import duke.DukeNukemException;
import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.state.GameplayState;
import duke.state.TitleScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class RestoreGameTest {
    private RestoreGame restoreGame;

    private GameSystems systems;

    @BeforeEach
    void create() {
        restoreGame = new RestoreGame();

        systems = GameSystemsFixture.create();
    }

    @ParameterizedTest
    @ValueSource(chars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'})
    void shouldRestoreGame(char slot) {
        when(systems.getKeyHandler().consume()).thenReturn((int) slot);

        restoreGame.update(systems);

        verify(systems.getAssets()).loadGame(slot);
        verify(systems.getStateRequester()).requestState(isA(GameplayState.class));
    }

    @Test
    void shouldRejectInvalidGameNumber() {
        when(systems.getAssets().loadGame(anyChar())).thenThrow(new DukeNukemException("Game not found"));

        restoreGame.update(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
        verifyNoInteractions(systems.getStateRequester());
    }

    @Test
    void shouldGoBackToTitleScreenOnOtherInput() {
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        restoreGame.update(systems);

        verify(systems.getStateRequester()).requestState(isA(TitleScreen.class));
    }
}