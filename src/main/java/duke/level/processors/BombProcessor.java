package duke.level.processors;

import duke.gameplay.active.items.Bomb;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.TOP;

public class BombProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        // TODO on the final level this copies a tile from above but it looks off.
        // That one falls down. Maybe that needs to grab the tile from the left?
        builder.add(new Bomb(x, y)).replaceTile(index, TOP);
    }

    static final int TILE_ID = 0x3057;
}
