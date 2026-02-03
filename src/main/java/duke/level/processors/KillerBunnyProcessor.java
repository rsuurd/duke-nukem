package duke.level.processors;

import duke.gameplay.active.enemies.KillerBunny;
import duke.level.Level;
import duke.level.LevelBuilder;

public class KillerBunnyProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new KillerBunny(x, y)).replaceTile(index, LevelBuilder.LEFT);
    }

    static final int TILE_ID = 0x303b;
}
