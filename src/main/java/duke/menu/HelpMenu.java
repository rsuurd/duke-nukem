package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;
import duke.ui.KeyHandler;

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
        KeyHandler keys = systems.getKeyHandler();

        if (keys.consume(VK_S)) {
            open(new SaveGame(context), systems);
        } else if (keys.consume(VK_I)) {
            open(new Instructions(), systems);
        } else if (keys.consume(VK_G)) {
            open(new GameSetup(), systems);
        } else if (keys.consume(VK_H)) {
            open(new HighScores(), systems);
        } else if (keys.consume(VK_F10)) {
            open(new Confirmation(TILE_SIZE, TILE_SIZE, """
                Are you sure you want to
                RESTART the level(Y/N)?
                """, () -> System.err.println("Restarting level...")), systems);
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
