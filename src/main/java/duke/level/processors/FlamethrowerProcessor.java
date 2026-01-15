package duke.level.processors;

import duke.gameplay.Facing;
import duke.gameplay.active.Flamethrower;
import duke.level.Level;
import duke.level.LevelBuilder;

public class FlamethrowerProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == RIGHT_TILE_ID || tileId == LEFT_TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        Facing facing = tileId == RIGHT_TILE_ID ? Facing.RIGHT : Facing.LEFT;
        int offset = tileId == RIGHT_TILE_ID ? LevelBuilder.RIGHT : LevelBuilder.LEFT;

        builder.add(new Flamethrower(Level.toX(index), Level.toY(index), facing)).replaceTile(index, offset);
    }

    static final int RIGHT_TILE_ID = 0x3009;
    static final int LEFT_TILE_ID = 0x300A;
}
