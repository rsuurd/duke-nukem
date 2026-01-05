package duke.gameplay;

import duke.gameplay.effects.Score;

public class ScoreManager {
    private int score;

    private ActiveManager activeManager;

    public ScoreManager(ActiveManager activeManager) {
        this.activeManager = activeManager;

        this.score = 0;
    }

    public void score(int points) {
        score += points;
    }

    public void score(int points, int x, int y) {
        score(points);

        if (Score.supports(points)) {
            activeManager.spawn(new Score(x, y, points));
        }
    }

    public int getScore() {
        return score;
    }
}
