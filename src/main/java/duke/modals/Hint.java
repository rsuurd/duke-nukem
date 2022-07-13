package duke.modals;

import static duke.Gfx.TILE_SIZE;

public class Hint extends Modal {
    public Hint(String text) {
        super(TILE_SIZE, 48, text, EXIT_ON_ENTER);
    }
}
