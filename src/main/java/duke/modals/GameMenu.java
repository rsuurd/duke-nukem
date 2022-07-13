package duke.modals;

import duke.GameState;
import duke.KeyHandler;

import static duke.Gfx.TILE_SIZE;
import static java.awt.event.KeyEvent.*;

public class GameMenu extends Modal {
    private static final String TEXT = "\n" +
            "      DUKE HELP MENU\n" +
            "      --------------\n\n" +
            "      S)ave a Game\n" +
            "      I)instructions\n" +
            "      G)ame setup\n" +
            "      H)igh scores\n" +
            "    F10) Restart Level\n";

    private static final String SAVE_IN_HALLWAYS = "\nYou can only save a game\n in the main hallways.";
    private static final String INSTRUCTIONS = "      Instructions\n" +
            "Important Keys:\n" +
            "  CTRL - Jump\n" +
            "  ALT  - Fire\n" +
            "  Cursor Keys - Move Man\n" +
            "  Up Cursor - Action Key\n\n" +
            "Change keys by choosing\noption \"K\" in HELP MENU.\n\n" +
            "  \"S\" - Toggle Sound\n" +
            "  \"H\" - Toggle Hint Mode\n" +
            "  F10 - Restart Level\n" +
            "  ESC - Quit Game\n" +
            "  \"<\" - Decrease Speed\n" +
            "  \">\" - Increase Speed\n\n" +
            "     Press any key.";

    public GameMenu() {
        super(TILE_SIZE, TILE_SIZE, TEXT, DO_NOTHING); // TODO EXIT_ON_ESC?
    }

    public void handleInput(GameState state, KeyHandler handler) {
        if (handler.isPressed(VK_S)) {
            if (state.getLevel().isIntermission()) {
                state.showModal(new SaveMenu());
            } else {
            state.showModal(new Modal(getX(), getY() + TILE_SIZE, SAVE_IN_HALLWAYS, EXIT_ON_ANY_KEY));
            }
        } else if (handler.isPressed(VK_I)) {
            state.showModal(new Modal(getX(), getY(), INSTRUCTIONS, EXIT_ON_ANY_KEY));
        } else if (handler.isPressed(VK_F10)) {
            state.showModal(new Confirmation(getX(), getY(), "Are you sure you want to\nRESTART the level(Y/N)?", state::resetLevel));
        } else if (handler.isPressed(VK_ESCAPE)) {
            state.closeModals();
        }
    }

    private static class SaveMenu extends Modal {
        private static final String TEXT = "       Save a game.\n     Which game number\n  do you want to save?\n     Choose (1-9): ";

        public SaveMenu() {
            super(TILE_SIZE, 48, TEXT, DO_NOTHING);
        }

        @Override
        public void handleInput(GameState state, KeyHandler handler) {
            int pressedKey = handler.getPressedKey();

            if ((pressedKey >= '1') && (pressedKey <= '9')) {
                state.save((char) pressedKey);

                state.showModal(new Modal(getX(), getY(), "   Your game is SAVED.\n      Press ENTER:", EXIT_ON_ENTER));
            } else if (pressedKey > 0) {
                state.showModal(new Modal(getX(), getY(), "    Incorrect number.\nYour game was NOT saved.\n\n      Press ENTER:", EXIT_ON_ENTER));
            }
        }
    }
}
