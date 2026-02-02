package duke.level.processors;

import duke.gameplay.active.AccessCardActivationPoint;
import duke.level.Level;
import duke.level.LevelBuilder;

public class AccessCardActivationPointProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new AccessCardActivationPoint(x, y));
    }

    static final int TILE_ID = 0x3034;
}
