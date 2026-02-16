package duke.level.processors;

import duke.gameplay.active.Water;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.BOTTOM;
import static duke.level.LevelBuilder.LEFT;

public class WaterProcessor implements ActiveProcessor {
    static final int TILE_ID = 0x3014;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new Water(x, y)).replaceTile(index, LEFT);
    }
}
