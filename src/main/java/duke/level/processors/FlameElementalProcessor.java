package duke.level.processors;

import duke.gameplay.Facing;
import duke.gameplay.active.enemies.FlameElemental;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.LEFT;

public class FlameElementalProcessor implements ActiveProcessor {
    static final int TILE_ID = 0x303c;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new FlameElemental(x, y, Facing.LEFT)).replaceTile(index, LEFT);
    }
}
