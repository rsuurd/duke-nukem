package duke.level.processors;

import duke.gameplay.active.Arc;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.Level.TILE_SIZE;
import static duke.level.LevelBuilder.TOP;

public class ArcProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        int cols = 0;
        while (canProcess(builder.getTileId(index + cols))) {
            builder.replaceTile(index + cols, TOP);

            cols++;
        }

        builder.add(new Arc(x, y, (cols * TILE_SIZE)));
    }

    static final int TILE_ID = 0x301a;
}
