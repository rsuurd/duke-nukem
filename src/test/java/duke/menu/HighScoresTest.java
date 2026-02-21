package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.resources.HighScoreLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HighScoresTest {
    private HighScores highScores;

    private GameSystems systems;

    @BeforeEach
    void createInstructions() {
        highScores = new HighScores(0);

        systems = GameSystemsFixture.create();
    }

    @Test
    void shouldOpen() {
        when(systems.getAssets().getHighScores()).thenReturn(List.of(new HighScoreLoader.HighScore("DUKE", 100000)));

        highScores.open(systems);

        ArgumentCaptor<Dialog> dialogCaptor = ArgumentCaptor.forClass(Dialog.class);
        verify(systems.getDialogManager()).open(dialogCaptor.capture());

        assertThat(dialogCaptor.getValue().message()).contains("1.").contains("DUKE").contains("100000");
    }

    @Test
    void shouldShowTop8() {
        when(systems.getAssets().getHighScores()).thenReturn(
                IntStream.range(0, 20).mapToObj(score -> new HighScoreLoader.HighScore("AAAA", score)).toList()
        );

        highScores.open(systems);

        ArgumentCaptor<Dialog> dialogCaptor = ArgumentCaptor.forClass(Dialog.class);
        verify(systems.getDialogManager()).open(dialogCaptor.capture());

        assertThat(dialogCaptor.getValue().message()).hasLineCount(18);
    }

    @Test
    void shouldClose() {
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        highScores.update(systems);

        verify(systems.getMenuManager()).closeAll(systems);
    }
}
