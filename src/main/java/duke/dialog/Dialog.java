package duke.dialog;

import static duke.level.Level.TILE_SIZE;

public record Dialog(String message, int x, int y, int rows, int cols, boolean showCursor) {
    public static Dialog create(String message, int x, int y, boolean showCursor) {
        int rows = calculateRowsNeeded(message);

        return new Dialog(message, x, y, rows, COLS, showCursor);
    }

    public static Dialog create(String message, boolean showCursor) {
        int rows = calculateRowsNeeded(message);

        int x = 160 - (COLS * TILE_SIZE) / 2;
        int y = 100 - (rows * TILE_SIZE) / 2;

        return new Dialog(message, x, y, rows, COLS, showCursor);
    }

    public static Dialog notes(String message) {
        return new Dialog(message, TILE_SIZE, TILE_SIZE, 4,  COLS, true);
    }

    public static Dialog hint(String hint) {
        return new Dialog(hint, TILE_SIZE, 3 * TILE_SIZE, 3,  COLS, true);
    }

    private static int calculateRowsNeeded(String message) {
        int lines = message.split("\n").length;
        return 2 + lines / 2 + (lines % 2);
    }

    static final int COLS = 13;
}
