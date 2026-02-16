package duke.gfx;

import duke.gameplay.Movable;
import duke.gameplay.player.Player;
import duke.level.Level;

import static duke.level.Level.TILE_SIZE;

public class Viewport {
    public static final int WIDTH = 224;
    public static final int HEIGHT = 176;

    static final int RIGHT_CAP = Level.WIDTH * TILE_SIZE - WIDTH;

    static final int LEFT_BOUND = 64;
    static final int RIGHT_BOUND = 144; // 224 - TILE_SIZE - 64

    static final int UPPER_BOUND = 32;
    static final int LOWER_BOUND = 112;
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

    public void update(Movable target) {
        if (shouldSmoothCenterVertically(target)) {
            smoothCenterVertically(target);
        }

        trackHorizontally(target);
        trackVertically(target);
    }

    private boolean shouldSmoothCenterVertically(Movable target) {
        // Center when we're tracking a player who's not jumping or falling ;)
        if (target instanceof Player player) {
            return switch (player.getState()) {
                case STANDING, WALKING, CLINGING -> true;
                default -> false;
            };
        }

        return false;
    }

    public void center(Movable target) {
        // TODO center around middle of target instead of top-left corner
        // x y is the camera's topleft though
        x = target.getX() - HORIZONTAL_CENTER;
        y = target.getY() - VERTICAL_CENTER;
    }

    private void smoothCenterVertically(Movable target) {
        int cameraY = target.getY() - VERTICAL_CENTER;
        int distance = cameraY - y;

        int scroll = Math.min(Math.abs(distance), CENTERING_SPEED);
        y += scroll * Integer.signum(distance);
    }

    private void trackHorizontally(Movable target) {
        if (target.getX() - x < LEFT_BOUND) {
            x = Math.max(target.getX() - LEFT_BOUND, 0);
        } else if (target.getX() + target.getWidth() - x > RIGHT_BOUND) {
            x = Math.min(target.getX() + target.getWidth() - RIGHT_BOUND, RIGHT_CAP);
        }
    }

    private void trackVertically(Movable target) {
        int top = y + UPPER_BOUND;
        int bottom = y + LOWER_BOUND;

        if (target.getY() < top) {
            y = target.getY() - UPPER_BOUND;
        } else if (target.getY() > bottom) {
            y = target.getY() - LOWER_BOUND;
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
