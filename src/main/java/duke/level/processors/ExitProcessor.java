package duke.level.processors;

import duke.gameplay.active.Exit;
import duke.level.Level;
import duke.level.LevelBuilder;

public class ExitProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == EXIT_TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder.add(new Exit(Level.toX(index), Level.toY(index) - Level.TILE_SIZE));
    }

    static final int EXIT_TILE_ID = 0x3011;
}
