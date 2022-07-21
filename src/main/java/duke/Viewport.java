package duke;

import duke.GameState;
import duke.active.Active;

import static duke.Gfx.TILE_SIZE;

public class Viewport {
    public static final int LEFT_BOUND = 88;
    public static final int RIGHT_BOUND = 120;
    public static final int UPPER_BOUND = 48;
    public static final int LOWER_BOUND = 112;
    public static final int VERTICAL_CENTER = 96;

    private int x;
    private int y;

    public void update(GameState state) {
        Duke duke = state.getDuke();

        int screenX = duke.getX() - x;

        if (screenX < LEFT_BOUND) {
            x = duke.getX() - LEFT_BOUND;
        } else if (screenX > RIGHT_BOUND) {
            x = duke.getX() - RIGHT_BOUND;
        }

        int screenY = duke.getY() - y;

        if (screenY < UPPER_BOUND) {
            y = duke.getY() - UPPER_BOUND;
        } else if (screenY > LOWER_BOUND) {
            y = duke.getY() - LOWER_BOUND;
        }

        if ((duke.getState() == Duke.State.STAND) || (duke.getState() == Duke.State.WALK)) {
            int distance = screenY - VERTICAL_CENTER;
            int multiplier = Math.min(Math.abs(distance), TILE_SIZE);

            y += (multiplier * Integer.signum(distance));
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible(Active active) {
        return active.collidesWith(x, y,  240, 192);
    }
}
