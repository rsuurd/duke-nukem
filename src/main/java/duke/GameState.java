package duke;

public class GameState {
    private Duke duke;
    private Level level;

    public void switchLevel(Level level) {
        duke = new Duke();

        this.level = level;

        duke.reset();
        duke.moveTo(level.getPlayerStartX(), level.getPlayerStartY());
    }

    public Level getLevel() {
        return level;
    }

    public int getCameraX() {
        return duke.getX() - 88;
    }

    public int getCameraY() {
        return duke.getY() - 64;
    }

    public Duke getDuke() {
        return duke;
    }

    public void update() {
        duke.update(this);
    }
}
