package duke.level.processors;

import duke.gameplay.active.enemies.BouncingMine;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.LEFT;

public class BouncingMineProcessor implements ActiveProcessor {
    static final int BOUNCING_TILE_ID = 0x3031;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == BOUNCING_TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new BouncingMine(x, y)).replaceTile(index, LEFT);
    }
}
