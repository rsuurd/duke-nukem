package duke.state;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gfx.Font;
import duke.gfx.Sprite;
import duke.menu.HighScores;
import duke.resources.HighScoreLoader;
import duke.sfx.Sfx;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static duke.level.Level.HALF_TILE_SIZE;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_ENTER;

public class GameOver implements GameState {
    private int score;

    private Font font;

    private List<HighScoreLoader.HighScore> highScores;
    private boolean enteringName;
    private StringBuilder name;

    private List<Sprite> cursor;
    private int tick;

    public GameOver(int score) {
        this.score = score;

        name = new StringBuilder();
    }

    @Override
    public void start(GameSystems systems) {
        font = new Font(systems.getAssets());

        highScores = new LinkedList<>(systems.getAssets().getHighScores());
        enteringName = isHighScore();
        cursor = systems.getAssets().getObjects().subList(85, 89);

        if (enteringName) {
            systems.getSoundManager().play(Sfx.HIGH_SCORE);
            systems.getDialogManager().open(NAME_ENTRY);
        } else {
            systems.getSoundManager().play(Sfx.PLAYER_QUIT);
            showHighScores(systems);
        }
    }

    @Override
    public void update(GameSystems systems) {
        if (enteringName) {
            inputName(systems);
        } else if (systems.getKeyHandler().consumeAny()) {
            systems.getStateRequester().requestState(new TitleScreen());
        }
    }

    private void showHighScores(GameSystems systems) {
        // TODO build and show high scores dialog

        systems.getMenuManager().open(new HighScores(56), systems);
    }

    private void inputName(GameSystems systems) {
        tick = (tick + 1) % 4;

        int keyCode = systems.getKeyHandler().consume();

        if (keyCode == VK_BACK_SPACE) {
            int newLength = name.isEmpty() ? 0 : name.length() - 1;
            name.setLength(newLength);
        }

        if (keyCode >= 32 && keyCode <= 122) {
            if (name.length() >= 10) {
                name.setLength(9);
            }

            name.append((char) keyCode);
        }

        if (keyCode == VK_ENTER) {
            enteringName = false;

            highScores.add(new HighScoreLoader.HighScore(name.toString(), score));
            highScores.sort(Comparator.comparingInt(HighScoreLoader.HighScore::score).reversed());

            systems.getAssets().saveHighScores(highScores);
            systems.getDialogManager().close();
            showHighScores(systems);
        }
    }

    @Override
    public void render(GameSystems systems) {
        systems.getDialogManager().render(systems.getRenderer());

        if (enteringName) {
            renderNameEntryPrompt(systems);
        }
    }

    private boolean isHighScore() {
        return highScores.stream().limit(8).anyMatch(highScore -> score > highScore.score());
    }

    private void renderNameEntryPrompt(GameSystems systems) {
        int x = 120;
        int y = 88;

        for (int i = 0; i < 10; i++) {
            if (i < name.length()) {
                font.drawText(systems.getRenderer(), Character.toString(name.charAt(i)), x, y);
            } else if (i == name.length()) {
                systems.getRenderer().draw(cursor.get(tick), x, y);
            } else {
                font.drawText(systems.getRenderer(), "-", x, y);
            }

            x += HALF_TILE_SIZE;
        }
    }

    private static final Dialog NAME_ENTRY = new Dialog("""
                   HIGH SCORE
            ------------------------
            
            Enter your name into the
            Duke Nukum Hall of Fame.
            
            
            
            Press ENTER when done.
            
            """, 56, 32, 6, 13, false, false);
}
