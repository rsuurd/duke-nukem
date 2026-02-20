package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;

import static duke.level.Level.TILE_SIZE;
import static java.awt.event.KeyEvent.*;

public class HelpMenu implements Menu {
    private GameplayContext context;

    public HelpMenu(GameplayContext context) {
        this.context = context;
    }

    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(HELP_DIALOG);
    }

    @Override
    public void update(GameSystems systems) {
        switch (systems.getKeyHandler().getPressedKey()) {
            case VK_S -> open(new SaveGame(context), systems);
            case VK_I -> open(new Instructions(), systems);
            case VK_G -> open(new GameSetup(context), systems);
            case VK_H -> open(new HighScores(), systems);
            case VK_F10 -> open(new Restart(context), systems);
        }
    }

    private void open(Menu menu, GameSystems systems) {
        systems.getMenuManager().open(menu, systems);
    }

    private static final Dialog HELP_DIALOG = new Dialog("""
            
                 DUKE HELP MENU
                 --------------
            
                 S)ave a game
                 I)nstructions
                 G)ame Setup
                 H)igh scores
               F10) Restart level
            
            """, TILE_SIZE, TILE_SIZE, 6, 13, true, false);
}
