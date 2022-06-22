package duke.active;

import duke.Duke;
import duke.GameState;
import duke.Level;

import static duke.Gfx.TILE_SIZE;

public class Lock extends Active {
    public static final int RED_LOCK_TILE_ID = 0x3048;
    public static final int GREEN_LOCK_TILE_ID = 0x3049;
    public static final int BLUE_LOCK_TILE_ID = 0x304A;
    public static final int MAGENTA_LOCK_TILE_ID = 0x304B;

    public static final int OFF_LOCK_TILE_ID = 0x10000;

    private Key.Type type;

    private boolean unlocked;
    private int frame;

    public Lock(int x, int y, Key.Type type) {
        super(x, y);

        this.type = type;

        unlocked = false;
        frame = 0;
    }

    @Override
    public void update(GameState state) {
        Duke duke = state.getDuke();

        if (duke.collidesWith(this) && duke.isUsing() && state.getInventory().hasKey(type)) {
            unlock(state);
        }

        if (!unlocked) {
            frame = (frame + 1) % 8;

            updateBackgroundTile(state.getLevel());
        }
    }

    private void unlock(GameState state) {
        for (Active active: state.getLevel().getActives()) {
            if (active instanceof Door) {
                Door door = (Door) active;

                if (door.getType() == type) {
                    door.open(state);
                }
            }
        }

        unlocked = true;
        frame = 0;

        state.getInventory().useKey(type);
        updateBackgroundTile(state.getLevel());
    }

    private void updateBackgroundTile(Level level) {
        int tileId = (frame < 4) ? RED_LOCK_TILE_ID + type.ordinal() : OFF_LOCK_TILE_ID;

        level.setTile(y / TILE_SIZE, x / TILE_SIZE, tileId);
    }
}
