package duke.level.processors;

import duke.gameplay.active.Girder;
import duke.level.Level;
import duke.level.LevelBuilder;

public class GirderTileProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new Girder(x, y)).setTile(index, Girder.GIRDER_BLOCK_TILE_ID);
    }

    static final int TILE_ID = 0x3036;
}
