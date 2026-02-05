package duke.level.processors;

import duke.gameplay.active.enemies.Helicopter;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.Level.TILE_SIZE;

public class HelicopterProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index) - TILE_SIZE;

        builder.add(new Helicopter(x, y)).replaceTile(index, LevelBuilder.LEFT);
    }

    static final int TILE_ID = 0x3022;
}
