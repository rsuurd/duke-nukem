package duke;

public class GameState {
    private Level level;

    private int cameraX;
    private int cameraY;

    public void switchLevel(Level level) {
        this.level = level;

        cameraX = 0;
        cameraY = 0;
    }

    public Level getLevel() {
        return level;
    }

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public void moveCamera(int deltaX, int deltaY) {
        cameraX += deltaX;
        cameraY += deltaY;
    }
}
