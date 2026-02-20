package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;

import java.util.List;

import static duke.level.Level.TILE_SIZE;

public class HighScores implements Menu {
    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(createHighScoresDialog());
    }

    @Override
    public void update(GameSystems systems) {
        if (systems.getKeyHandler().isAnyKeyPressed()) {
            systems.getMenuManager().closeAll(systems);
        }
    }

    private Dialog createHighScoresDialog() {
        // TODO read these from assetmanager
        List<HighScore> highScores = List.of(
                new HighScore("TODD", 40000),
                new HighScore("SCOTT", 30000),
                new HighScore("GEORGE", 20000),
                new HighScore("AL", 10000),
                new HighScore("JOHN", 500),
                new HighScore("ROLF", 100),
                new HighScore("", 0),
                new HighScore("", 0)
        );

        StringBuilder builder = new StringBuilder("  --- HIGH  SCORES ---\n\n");

        for (int i = 0; i < highScores.size(); i++) {
            HighScore entry = highScores.get(i);

            builder.append(String.format(" %d.  %-7d %s\n\n", i + 1, entry.score, entry.name));
        }

        return new Dialog(builder.toString(), TILE_SIZE, TILE_SIZE, 10, 13, true, false);
    }

    private record HighScore(String name, int score) {
    }
}
