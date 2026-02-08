package duke.gameplay.active;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gameplay.effects.EffectsFactory;
import duke.level.Level;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class Girder extends Active implements Updatable {
    private boolean extending;
    private boolean fullyExtended;

    public Girder(int x, int y) {
        super(x, y, TILE_SIZE, TILE_SIZE);

        extending = false;
        fullyExtended = false;
    }

    @Override
    public void update(GameplayContext context) {
        if (extending && !fullyExtended) {
            int row = getRow();
            int col = (getX() + getWidth()) / TILE_SIZE;

            if (canExtendFurther(context.getLevel().getTile(row, col))) {
                setWidth(getWidth() + TILE_SIZE);
                context.getLevel().setTile(getRow(), col, GIRDER_BLOCK_TILE_ID);
                context.getActiveManager().spawn(EffectsFactory.createSlowFlash(col * TILE_SIZE, getY()));
            } else {
                fullyExtended = true;
            }
        }
    }

    public void extend(GameplayContext context) {
        if (extending) return;

        extending = true;
        context.getSoundManager().play(Sfx.BRIDGE_EXTEND);
    }

    private boolean canExtendFurther(int tileId) {
        return (!Level.isSolid(tileId) || tileId == GIRDER_BLOCK_TILE_ID);
    }

    public static final int GIRDER_BLOCK_TILE_ID = 0x2fe0;
}
