package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.awt.event.KeyEvent.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class MainMenuTest {
    private MainMenu mainMenu;

    private GameSystems systems;

    @BeforeEach
    void create() {
        mainMenu = new MainMenu();

        systems = spy(GameSystemsFixture.create());
    }

    @Test
    void shouldOpen() {
        mainMenu.open(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldStartNewGame() {
        mainMenu.open(systems);

        when(systems.getKeyHandler().consume(VK_S)).thenReturn(true);

        mainMenu.update(systems);

        // verify we fade to black and start the Prologue
    }

    @Test
    void shouldShowInstructions() {
        mainMenu.open(systems);

        when(systems.getKeyHandler().consume(VK_I)).thenReturn(true);

        mainMenu.update(systems);

        // verify we fade to black and request the Instructions State
    }

    @Test
    void shouldShowGameSetup() {
        mainMenu.open(systems);

        when(systems.getKeyHandler().consume(VK_G)).thenReturn(true);

        mainMenu.update(systems);

        verify(systems.getMenuManager()).open(isA(GameSetup.class), same(systems));
    }

    @Test
    void shouldShowHighScores() {
        mainMenu.open(systems);

        when(systems.getKeyHandler().consume(VK_H)).thenReturn(true);

        mainMenu.update(systems);

        verify(systems.getMenuManager()).open(isA(HighScores.class), same(systems));
    }

    @Test
    void shouldShowTitleScreen() {
        mainMenu.open(systems);

        when(systems.getKeyHandler().consume(VK_I)).thenReturn(true);

        mainMenu.update(systems);

        // verify we fade to black and request the TitleScreen State
    }

    @Test
    void shouldShowCredits() {
        mainMenu.open(systems);

        when(systems.getKeyHandler().consume(VK_C)).thenReturn(true);

        mainMenu.update(systems);

        // verify we fade to black and request the Credits State
    }
}
