package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;
import duke.ui.KeyHandler;

import static duke.level.Level.TILE_SIZE;
import static java.awt.event.KeyEvent.VK_Y;

public class Restart implements Menu {
    private GameplayContext context;

    public Restart(GameplayContext context) {
        this.context = context;
    }

    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(CONFIRM_RESTART);
    }

    @Override
    public void update(GameSystems systems) {
        KeyHandler handler = systems.getKeyHandler();

        if (handler.isPressed(VK_Y)) {
            restart(systems);
        } else if (handler.isAnyKeyPressed()) {
            close(systems);
        }
    }

    private void restart(GameSystems systems) {
        // TODO go back to hallway, or GetReadyState if level 1

        close(systems);
    }

    private void close(GameSystems systems) {
        systems.getMenuManager().closeAll(systems);
    }

    private static final Dialog CONFIRM_RESTART = new Dialog("""
            Are you sure you want to
            RESTART the level(Y/N)?
            """, TILE_SIZE, TILE_SIZE, 2, 13, true, false);
}
