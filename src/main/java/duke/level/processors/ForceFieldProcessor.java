package duke.level.processors;

import duke.gameplay.active.ForceField;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.Level.TILE_SIZE;
import static duke.level.LevelBuilder.LEFT;

public class ForceFieldProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        int rows = 0;
        while (canProcess(builder.getTileId(index + (rows * Level.WIDTH)))) {
            builder.replaceTile(index + (rows * Level.WIDTH), LEFT);

            rows++;
        }

        builder.add(new ForceField(x, y, rows * TILE_SIZE));
    }

    static final int TILE_ID = 0x3021;
}
