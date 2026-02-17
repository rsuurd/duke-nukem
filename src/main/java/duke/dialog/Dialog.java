package duke.dialog;

import static duke.level.Level.TILE_SIZE;

public record Dialog(String message, int x, int y, int rows, int cols, boolean showCursor, boolean closeOnEnter) {
    public static Dialog notes(String message) {
        return new Dialog(message + PRESS_ENTER, TILE_SIZE, TILE_SIZE, 4, COLS, true, true);
    }

    public static Dialog hint(String hint) {
        return new Dialog(hint + PRESS_ENTER, TILE_SIZE, 3 * TILE_SIZE, 3, COLS, true, true);
    }

    public static final int COLS = 13;

    static final String PRESS_ENTER = "\n      Press ENTER:";
}
