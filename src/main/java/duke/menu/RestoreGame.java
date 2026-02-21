package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.state.TitleScreen;
import duke.ui.KeyHandler;

import static duke.menu.MainMenu.MAIN_MENU_X;

public class RestoreGame implements Menu {
    private boolean error;

    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(new Dialog("""
                     Restore a game
                   Which game number
                 do you want to load?
                     Choose (1-9):
                """, MAIN_MENU_X, 48, 3, 13, true, false));
    }

    @Override
    public void update(GameSystems systems) {
        if (!error) {
            int slot = getSelectedGameNumber(systems.getKeyHandler());

            if (slot > 0) {
                restoreGame(slot, systems);
            }
        }

        if (systems.getKeyHandler().consumeAny()) {
            systems.getStateRequester().requestState(new TitleScreen());
        }
    }

    private int getSelectedGameNumber(KeyHandler keyHandler) {
        for (int i = 1; i <= 9; i++) {
            if (keyHandler.consume(i + '0')) {
                return i;
            }
        }

        return 0;
    }

    private void restoreGame(int slot, GameSystems systems) {
        error = true;
        systems.getDialogManager().open(new Dialog("    Game not found!!", MAIN_MENU_X, 32, 2, 13, false, false));
    }
}
