package duke.modals;

import duke.GameState;
import duke.KeyHandler;

import static java.awt.event.KeyEvent.VK_Y;

public class Confirmation extends Modal {
    private Runnable confirmed;

    public Confirmation(int x, int y, String text, Runnable confirmed) {
        super(x, y, text, true);

        this.confirmed = confirmed;
    }

    @Override
    public void handleInput(GameState state, KeyHandler handler) {
        if (handler.isPressed(VK_Y)) {
            confirmed.run();
        } else if (handler.isAnyKeyPressed()) {
            state.closeModals();
        }
    }
}
