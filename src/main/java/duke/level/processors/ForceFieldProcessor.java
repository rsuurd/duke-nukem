package duke.level.processors;

import duke.gameplay.active.ForceField;
import duke.level.Level;
import duke.level.LevelBuilder;

public class ForceFieldProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new ForceField(x, y)).replaceTile(index, LevelBuilder.LEFT);
    }

    static final int TILE_ID = 0x3021;
}
