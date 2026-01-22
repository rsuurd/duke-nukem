package duke.level.processors;

import duke.gameplay.active.enemies.Ed209;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.LEFT;

public class Ed209Processor implements ActiveProcessor {
    public static final int TILE_ID = 0x300c;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new Ed209(x, y)).replaceTile(index, LEFT);
    }
}
