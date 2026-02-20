package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.ui.KeyHandler;

import static duke.level.Level.TILE_SIZE;
import static java.awt.event.KeyEvent.*;

public class GameSetup implements Menu {
    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(GAME_SETUP);
    }

    @Override
    public void update(GameSystems systems) {
        KeyHandler handler = systems.getKeyHandler();

        if (handler.consume(VK_J)) {
            openJoystickMode(systems);
        } else if (handler.consume(VK_K)) {
            // openKeyboardMode(systems);
        } else if (handler.consumeAll(VK_ESCAPE)) {
            systems.getMenuManager().closeAll(systems);
        }
    }

    private void openJoystickMode(GameSystems systems) {
        systems.getDialogManager().open(JOYSTICK_MODE);
    }

    // TODO these can be triggered from outside the menu as well, so should probably just delegate to context

    private static final Dialog GAME_SETUP = new Dialog("""
            
                  GAME SETUP
            
                J)oystick mode
                K)eyboard mode
                S)ound toggle
                H)int toggle
            """, TILE_SIZE, 2 * TILE_SIZE, 5, 13, true, false);

    private static final Dialog JOYSTICK_MODE = new Dialog("""
                 Joystick mode
            
            Sorry, joystick support
            is not yet implemented.
            """, TILE_SIZE, 3 * TILE_SIZE, 4, 13, true, false);
}
