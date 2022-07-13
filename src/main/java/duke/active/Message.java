package duke.active;

import duke.Duke;
import duke.GameState;
import duke.modals.Modal;

import static duke.Gfx.TILE_SIZE;

public class Message extends Active {
    public Message(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(GameState state) {
        Duke duke = state.getDuke();

        if (duke.collidesWith(this)) {
            state.getHints().showHint(this);

            if (duke.isUsing()) {
                String message = getMessage(state.getLevel().getNumber());

                if (message != null) {
                    state.showModal(new Modal(TILE_SIZE, TILE_SIZE, message, Modal.EXIT_ON_ENTER));
                }
            }
        }
    }

    private String getMessage(int level) {
        return switch (level) {
            case 1 -> "You are entering the\nnext level. Now is a\ngood time to SAVE your\ngame.\n\n      Press ENTER:";
            default -> null;
        };
    }
}
