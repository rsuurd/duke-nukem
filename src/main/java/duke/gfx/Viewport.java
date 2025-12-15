package duke.gfx;

public class Viewport {
    static final int LEFT_BOUND = 88;
    static final int RIGHT_BOUND = 136;
    static final int UPPER_BOUND = 48;
    static final int LOWER_BOUND = 112;
    static final int HORIZONTAL_CENTER = 112;
    static final int VERTICAL_CENTER = 96;
    private static final int CENTERING_SPEED = 16;

    private int x;
    private int y;

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
            x = targetX - LEFT_BOUND;
        } else if (screenX > RIGHT_BOUND) {
            x = targetX - RIGHT_BOUND;
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
}
