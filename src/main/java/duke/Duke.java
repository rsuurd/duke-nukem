package duke;

public class Duke {
    private int x;
    private int y;

    private int velocityX;
    private int velocityY;

    public void accelerate(int deltaX, int deltaY) {
        velocityX += deltaX;
        velocityY += deltaY;
    }

    public void update(GameState state) {
        Level level = state.getLevel();

        moveHorizontally(level);
        moveVertically(level);
    }

    private void moveHorizontally(Level level) {
        int newPositionX = x + velocityX;

        if (!level.collides(newPositionX, y, 31, 31)) {
            x = newPositionX;
        }

        velocityX = 0;
    }

    private void moveVertically(Level level) {
        int newPositionY = y + velocityY;

        if (!level.collides(x, newPositionY, 31, 31)) {
            y = newPositionY;
        }

        velocityY = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
