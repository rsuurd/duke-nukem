package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;

import static duke.level.Level.TILE_SIZE;
import static java.awt.event.KeyEvent.VK_ESCAPE;

public class GameSetup implements Menu {
    private GameplayContext context;

    public GameSetup(GameplayContext context) {
        this.context = context;
    }

    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(GAME_SETUP);
    }

    @Override
    public void update(GameSystems systems) {
        switch (systems.getKeyHandler().getPressedKey()) {
            case 'J' -> openJoystickMode(systems);
//            case 'K' -> openKeyboardMode(systems);
            case 'S' -> toggleSound(systems);
            case 'H' -> toggleHints(systems);
            case VK_ESCAPE -> systems.getMenuManager().closeAll(systems);
        }
    }

    private void openJoystickMode(GameSystems systems) {
        systems.getDialogManager().open(JOYSTICK_MODE);
    }

    // TODO these can be triggered from outside the menu as well, so should probably just delegate to context
    private void toggleSound(GameSystems systems) {
        context.getSoundManager().toggle();

        systems.getDialogManager().open(new Dialog("       Sound toggle\n\n     The sound is off.", TILE_SIZE, 3 * TILE_SIZE, 3, 13, true, false));
    }

    private void toggleHints(GameSystems systems) {
        context.getHints().toggle();

        systems.getDialogManager().open(new Dialog("Hint toggle\n\nHints are off.", TILE_SIZE, 3 * TILE_SIZE, 3, 13, true, false));
    }

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
