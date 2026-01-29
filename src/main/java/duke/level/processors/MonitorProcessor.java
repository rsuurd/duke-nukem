package duke.level.processors;

import duke.gameplay.active.Monitor;
import duke.level.Level;
import duke.level.LevelBuilder;

public class MonitorProcessor implements ActiveProcessor {
    static final int TILE_ID = 0x3041;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new Monitor(x, y));
    }
}
