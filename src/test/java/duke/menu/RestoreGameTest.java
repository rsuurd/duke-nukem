package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.state.TitleScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestoreGameTest {
    private RestoreGame restoreGame;

    private GameSystems systems;

    @BeforeEach
    void create() {
        restoreGame = new RestoreGame();

        systems = GameSystemsFixture.create();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void shouldInputGameNumber(int number) {
        when(systems.getKeyHandler().consume(number + '0')).thenReturn(true);

        restoreGame.update(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldGoBackToTitleScreenOnOtherInput() {
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        restoreGame.update(systems);

        verify(systems.getStateRequester()).requestState(isA(TitleScreen.class));
    }
}