package duke.level.processors;

import duke.gameplay.active.Missile;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.Level.TILE_SIZE;
import static duke.level.LevelBuilder.TOP;

public class MissileProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder
                .add(new Missile(Level.toX(index), Level.toY(index) - (3 * TILE_SIZE)))
                .replaceTile(index, TOP);
    }

    static final int TILE_ID = 0x3007;
}
