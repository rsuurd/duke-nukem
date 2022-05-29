package duke;

public class GameState {
    private Duke duke;
    private Level level;

    private int score;

    public GameState() {
        score = 0;
    }

    public void switchLevel(Level level) {
        duke = new Duke();

        this.level = level;

        duke.reset();
        duke.moveTo(level.getPlayerStartX(), level.getPlayerStartY());
    }

    public Level getLevel() {
        return level;
    }

    public Duke getDuke() {
        return duke;
    }

    public int getScore() {
        return score;
    }

    public void update() {
        duke.update(this);
    }
}
