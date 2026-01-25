package duke.gfx;

import duke.gameplay.Movable;
import duke.level.Level;

import static duke.level.Level.TILE_SIZE;

public class Viewport {
    private static final int WIDTH = 224;
    private static final int HEIGHT = 176;

    static final int LEFT_CAP = -TILE_SIZE; // not 0 because the hud obscures one column of tiles
    static final int RIGHT_CAP = Level.WIDTH * TILE_SIZE - WIDTH;

    static final int LEFT_BOUND = 88;
    static final int RIGHT_BOUND = 136;
    static final int UPPER_BOUND = 48;
    static final int LOWER_BOUND = 112;
    static final int HORIZONTAL_CENTER = 112;
    static final int VERTICAL_CENTER = 96;
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
        int distance = toScreenY(targetY) - VERTICAL_CENTER;
        int scrollY = Math.min(Math.abs(distance), CENTERING_SPEED);

        y += (scrollY * Integer.signum(distance));
    }

    private void trackHorizontally(int targetX) {
        int screenX = toScreenX(targetX);

        if (screenX < LEFT_BOUND) {
            x = Math.max(targetX - LEFT_BOUND, LEFT_CAP);
        } else if (screenX > RIGHT_BOUND) {
            x = Math.min(targetX - RIGHT_BOUND, RIGHT_CAP);
        }
    }

    private void trackVertically(int targetY) {
        int screenY = toScreenY(targetY);

        if (screenY < UPPER_BOUND) {
            y = targetY - UPPER_BOUND;
        } else if (screenY > LOWER_BOUND) {
            y = targetY - LOWER_BOUND;
        }
    }

    public int toScreenX(int worldX) {
        return worldX - x;
    }

    public int toScreenY(int worldY) {
        return worldY - y;
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
