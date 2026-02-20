package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;

import static duke.level.Level.TILE_SIZE;

public class SaveGame implements Menu {
    private GameplayContext context;

    public SaveGame(GameplayContext context) {
        this.context = context;
    }

    @Override
    public void open(GameSystems systems) {
        if (!isInHallway()) {
            systems.getDialogManager().open(CAN_ONLY_SAVE_IN_HALLWAYS);
        }
    }

    @Override
    public void update(GameSystems systems) {
        if (systems.getKeyHandler().consumeAny()) {
            systems.getMenuManager().closeAll(systems);
        }
    }

    private boolean isInHallway() {
        return context.getLevel().getDescriptor().isHallway();
    }

    private static final Dialog CAN_ONLY_SAVE_IN_HALLWAYS = new Dialog("""
            
            You can only save a game
             in the main hallways.
            
            """, TILE_SIZE, 2 * TILE_SIZE, 3, 13, true, false);
}
