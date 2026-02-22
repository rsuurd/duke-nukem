package duke.menu;

import duke.DukeNukemException;
import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.SaveGame;
import duke.state.GameplayState;
import duke.state.TitleScreen;

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
            int keyCode = systems.getKeyHandler().consume();

            if (keyCode >= 0) {
                restoreGame((char) keyCode, systems);
            }
        }

        if (systems.getKeyHandler().consumeAny()) {
            systems.getStateRequester().requestState(new TitleScreen());
        }
    }

    private void restoreGame(char slot, GameSystems systems) {
        try {
            SaveGame saveGame = systems.getAssets().loadGame(slot);

            systems.getStateRequester().requestState(new GameplayState(saveGame));
        } catch (DukeNukemException e) {
            error = true;
            systems.getDialogManager().open(new Dialog("    Game not found!!", MAIN_MENU_X, 32, 2, 13, false, false));
        }
    }
}
