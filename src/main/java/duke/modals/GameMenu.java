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
        super(TILE_SIZE, TILE_SIZE, TEXT, true);
    }

    public void handleInput(GameState state, KeyHandler handler) {
        if (handler.isPressed(VK_S)) {
            state.showModal(new Message(getX(), getY() + TILE_SIZE, SAVE_IN_HALLWAYS, true));
        } else if (handler.isPressed(VK_I)) {
            state.showModal(new Message(getX(), getY(), INSTRUCTIONS, true));
        } else if (handler.isPressed(VK_F10)) {
            state.showModal(new Confirmation(getX(), getY(), "Are you sure you want to\nRESTART the level(Y/N)?", state::resetLevel));
        } else if (handler.isPressed(VK_ESCAPE)) {
            state.closeModals();
        }
    }
}
