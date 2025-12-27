package duke.level.processors;

import duke.gameplay.active.Acme;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.LEFT;

public class AcmeProcessor implements ActiveProcessor {
    public static final int TILE_ID = 0x302a;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new Acme(x, y)).replaceTile(index, LEFT);
    }
}
