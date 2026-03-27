package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.menu.HighScores;
import duke.resources.HighScoreLoader;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.awt.event.KeyEvent.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

class GameOverTest {
    private GameOver gameOver;

    private GameSystems systems;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();

        when(systems.getAssets().getObjects()).thenReturn(mock());
    }

    @Test
    void shouldShowHighScoresIfScoreTooLow() {
        gameOver = new GameOver(0);

        gameOver.start(systems);

        verify(systems.getSoundManager()).play(Sfx.PLAYER_QUIT);
        verify(systems.getMenuManager()).open(isA(HighScores.class), same(systems));
    }

    @Test
    void shouldGoToTitleScreen() {
        gameOver = new GameOver(0);
        gameOver.start(systems);

        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        gameOver.update(systems);
        verify(systems.getStateRequester()).requestState(isA(TitleScreen.class));
    }

    @Test
    void shouldAskNameIfHighScoreAchieved() {
        when(systems.getAssets().getHighScores()).thenReturn(
                List.of(new HighScoreLoader.HighScore("TODD", 50000)));

        gameOver = new GameOver(100000);

        gameOver.start(systems);

        verify(systems.getSoundManager()).play(Sfx.HIGH_SCORE);
        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldAddToHighScoresWhenNameConfirmed() {
        when(systems.getAssets().getHighScores()).thenReturn(
                List.of(new HighScoreLoader.HighScore("TODD", 50000)));

        gameOver = new GameOver(100000);

        gameOver.start(systems);

        verify(systems.getSoundManager()).play(Sfx.HIGH_SCORE);
        verify(systems.getDialogManager()).open(isA(Dialog.class));

        when(systems.getKeyHandler().consume()).thenReturn(VK_D, VK_U, VK_K, VK_E, VK_ENTER);

        gameOver.update(systems);
        gameOver.update(systems);
        gameOver.update(systems);
        gameOver.update(systems);
        gameOver.update(systems);

        verify(systems.getAssets()).saveHighScores(List.of(
                new HighScoreLoader.HighScore("DUKE", 100000),
                new HighScoreLoader.HighScore("TODD", 50000)
        ));

        verify(systems.getDialogManager()).close();
        verify(systems.getMenuManager()).open(isA(HighScores.class), same(systems));
    }
}
