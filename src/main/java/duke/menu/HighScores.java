package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.resources.HighScoreLoader;

import java.util.List;

import static duke.level.Level.TILE_SIZE;

public class HighScores implements Menu {
    private int x;

    public HighScores(int x) {
        this.x = x;
    }

    @Override
    public void open(GameSystems systems) {
        List<HighScoreLoader.HighScore> highScores = systems.getAssets().getHighScores();

        systems.getDialogManager().open(createHighScoresDialog(highScores));
    }

    @Override
    public void update(GameSystems systems) {
        if (systems.getKeyHandler().consumeAny()) {
            systems.getMenuManager().closeAll(systems);
        }
    }

    private Dialog createHighScoresDialog(List<HighScoreLoader.HighScore> highScores) {
        StringBuilder builder = new StringBuilder("  --- HIGH  SCORES ---\n\n");

        for (int i = 0; i < Math.min(MAX_HIGH_SCORES, highScores.size()); i++) {
            HighScoreLoader.HighScore entry = highScores.get(i);

            builder.append(String.format(" %d.  %-7d %s\n\n", i + 1, entry.score(), entry.name()));
        }

        return new Dialog(builder.toString(), x, TILE_SIZE, 10, 13, true, false);
    }

    private static final int MAX_HIGH_SCORES = 8;
}
