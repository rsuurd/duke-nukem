package duke.level.processors;

import duke.gameplay.active.enemies.DrProton;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.Level.TILE_SIZE;

public class DrProtonProcessor implements ActiveProcessor {
    static final int TILE_ID = 0x3043;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder
                .add(new DrProton(Level.toX(index), Level.toY(index) - TILE_SIZE))
                .replaceTile(index, LevelBuilder.LEFT);
    }
}
