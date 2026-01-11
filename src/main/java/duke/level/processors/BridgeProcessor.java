package duke.level.processors;

import duke.gameplay.active.Bridge;
import duke.level.Level;
import duke.level.LevelBuilder;

public class BridgeProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        int width = 0;
        while (width < Level.WIDTH && !Level.isSolid(builder.getTileId(index + width))) {
            width++;
        }

        builder.add(new Bridge(x, y, width * Level.TILE_SIZE));
    }

    static final int TILE_ID = 0x3019;
}
