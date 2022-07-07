package duke.modals;

import duke.GameState;
import duke.KeyHandler;

import static duke.Gfx.TILE_SIZE;
import static java.awt.event.KeyEvent.VK_ENTER;

public class Hint extends Modal {
    public Hint(String text) {
        super(TILE_SIZE, 48, text, true);
    }

    @Override
    public void handleInput(GameState state, KeyHandler handler) {
        if (handler.isPressed(VK_ENTER)) {
            state.closeModals();
        }
    }
}
