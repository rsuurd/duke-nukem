package duke.modals;

import duke.GameState;
import duke.KeyHandler;

public class Message extends Modal {
    public Message(int x, int y, String text, boolean prompt) {
        super(x, y, text, prompt);
    }

    @Override
    public void handleInput(GameState state, KeyHandler handler) {
        if (handler.isAnyKeyPressed()) {
            state.closeModals();
        }
    }
}
