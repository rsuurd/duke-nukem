package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;

import static duke.level.Level.TILE_SIZE;

public class Instructions implements Menu {
    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(INSTRUCTIONS);
    }

    @Override
    public void update(GameSystems systems) {
        if (systems.getKeyHandler().isAnyKeyPressed()) {
            systems.getMenuManager().closeAll(systems);
        }
    }

    private static final Dialog INSTRUCTIONS = new Dialog("""
                  Instructions
            Important Keys:
              CTRL - Jump
              ALT  - Fire
              Cursor Keys - Move Man
              Up Cursor - Action Key
            
            Change keys by choosing
            option "K" in HELP MENU.
            
              "S" - Toggle Sound
              "H" - Toggle Hint Mode
              F10 - Restart Level
              ESC - Quit Game
              "<" - Decrease Speed
              ">" - Increase Speed
            
                 Press any key.
            """, TILE_SIZE,  TILE_SIZE, 10, 13, true, false);
}
