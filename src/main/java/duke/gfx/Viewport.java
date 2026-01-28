package duke.gfx;

import duke.gameplay.Movable;
import duke.level.Level;

import static duke.level.Level.TILE_SIZE;

public class Viewport {
    public static final int WIDTH = 224;
    public static final int HEIGHT = 176;

    static final int RIGHT_CAP = Level.WIDTH * TILE_SIZE - WIDTH;

    static final int LEFT_BOUND = 80;
    static final int RIGHT_BOUND = 120;
    static final int UPPER_BOUND = 32;
    static final int LOWER_BOUND = 128;
    static final int HORIZONTAL_CENTER = 112;
    static final int VERTICAL_CENTER = 80;
    private static final int CENTERING_SPEED = 16;

    private int x;
    private int y;

    public Viewport() {
        this(0, 0);
    }

    Viewport(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(int targetX, int targetY, boolean shouldCenter) {
        if (shouldCenter) {
            centerVertically(targetY);
        }

        trackHorizontally(targetX);
        trackVertically(targetY);
    }

    public void center(int targetX, int targetY) {
        x = targetX - HORIZONTAL_CENTER;
        y = targetY - VERTICAL_CENTER;
    }

    private void centerVertically(int targetY) {
        int cameraY = targetY - VERTICAL_CENTER;
        int distance = cameraY - y;

        int scroll = Math.min(Math.abs(distance), CENTERING_SPEED);
        y += scroll * Integer.signum(distance);
    }

    private void trackHorizontally(int targetX) {
        int left = x + LEFT_BOUND;
        int right = x + RIGHT_BOUND;

        if (targetX < left) {
            x = Math.max(targetX - LEFT_BOUND, 0);
        } else if (targetX > right) {
            x = Math.min(targetX - RIGHT_BOUND, RIGHT_CAP);
        }
    }

    private void trackVertically(int targetY) {
        int top = y + UPPER_BOUND;
        int bottom = y + LOWER_BOUND;

        if (targetY < top) {
            y = targetY - UPPER_BOUND;
        } else if (targetY > bottom) {
            y = targetY - LOWER_BOUND;
        }
    }

    public int toScreenX(int worldX) {
        return TILE_SIZE + worldX - x;
    }

    public int toScreenY(int worldY) {
        return TILE_SIZE + worldY - y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible(Movable movable) {
        int movableRight = movable.getX() + movable.getWidth();
        int viewportRight = x + WIDTH;
        int movableBottom = movable.getY() + movable.getHeight();
        int viewportBottom = y + HEIGHT;

        return movableRight > x && movable.getX() < viewportRight && movableBottom > y && movable.getY() < viewportBottom;
    }
}
