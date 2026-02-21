package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.ui.KeyHandler;

import static duke.level.Level.TILE_SIZE;
import static java.awt.event.KeyEvent.*;

public class GameSetup implements Menu {
    private int x;

    public GameSetup(int x) {
        this.x = x;
    }

    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(new Dialog("""

                      GAME SETUP

                    J)oystick mode
                    K)eyboard mode
                    S)ound toggle
                    H)int toggle
                """, x, 2 * TILE_SIZE, 5, 13, true, false));
    }

    @Override
    public void update(GameSystems systems) {
        KeyHandler handler = systems.getKeyHandler();

        if (handler.consume(VK_J)) {
            openJoystickMode(systems);
        } else if (handler.consume(VK_K)) {
            openKeyboardMode(systems);
        } else if (handler.consumeAll(VK_ESCAPE)) {
            systems.getMenuManager().closeAll(systems);
        }
    }

    private void openJoystickMode(GameSystems systems) {
        systems.getDialogManager().open(new Dialog("""
                     Joystick mode

                Sorry, joystick support
                is not yet implemented.
                """, x, 3 * TILE_SIZE, 4, 13, true, false));
    }

    private void openKeyboardMode(GameSystems systems) {
        systems.getDialogManager().open(new Dialog("""
                     KEYBOARD MODE

                  1.  Up     :
                  2.  Down   :
                  3.  Left   :
                  4.  Right  :
                  5.  Fire   :
                  6.  Jump   :

                Sorry, changing keys
                is not yet implemented.
                """, x, 3 * TILE_SIZE, 8, 13, true, false));
    }
}
