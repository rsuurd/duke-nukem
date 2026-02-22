package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;
import duke.gameplay.SaveGameFactory;

import static duke.level.Level.TILE_SIZE;

public class SaveGame implements Menu {
    boolean error;

    private GameplayContext context;
    private SaveGameFactory factory;

    public SaveGame(GameplayContext context) {
        this(context, new SaveGameFactory());
    }

    SaveGame(GameplayContext context, SaveGameFactory factory) {
        this.context = context;
        this.factory = factory;
    }

    @Override
    public void open(GameSystems systems) {
        if (isInHallway()) {
            systems.getDialogManager().open(SAVE_A_GAME);
        } else {
            systems.getDialogManager().open(CAN_ONLY_SAVE_IN_HALLWAYS);
        }
    }

    @Override
    public void update(GameSystems systems) {
        if (isInHallway()) {
            int keyCode = systems.getKeyHandler().consume();

            if (keyCode >= 0) {
                if (error && keyCode == '\n') {
                    systems.getMenuManager().closeAll(systems);
                } else {
                    saveGame((char) keyCode, systems);
                }
            }
        } else {
            if (systems.getKeyHandler().consumeAny()) {
                systems.getMenuManager().closeAll(systems);
            }
        }
    }

    private boolean isInHallway() {
        return context.getLevel().getDescriptor().isHallway();
    }

    private void saveGame(char slot, GameSystems systems) {
        if (slot >= '1' && slot <= '9') {
            duke.gameplay.SaveGame saveGame = factory.create(context);

            systems.getAssets().saveGame(saveGame, slot);
            systems.getMenuManager().closeAll(systems);
        } else {
            error = true;
            systems.getDialogManager().open(INCORRECT_NUMBER);
        }
    }

    private static final Dialog CAN_ONLY_SAVE_IN_HALLWAYS = new Dialog("""
            
            You can only save a game
             in the main hallways.
            
            """, TILE_SIZE, 2 * TILE_SIZE, 3, 13, true, false);

    private static final Dialog SAVE_A_GAME = new Dialog("""
                 Save a game
               Which game number
              do you want to save?
                  Choose (1-9):
            """, TILE_SIZE, 3 * TILE_SIZE, 3, 13, true, false);

    private static final Dialog INCORRECT_NUMBER = new Dialog("""
               Incorrect number
            Your game was NOT saved.
            
                  Press ENTER:
            """, TILE_SIZE, 3 * TILE_SIZE, 3, 13, true, true);
}
