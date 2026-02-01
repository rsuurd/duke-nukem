package duke.level.processors;

import duke.gameplay.active.Transporter;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.Level.TILE_SIZE;

public class TransporterProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_IDS[0] || tileId == TILE_IDS[1];
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index) + TILE_SIZE;
        int y = Level.toY(index) - TILE_SIZE;

        builder.add(new Transporter(x, y)).replaceTile(index, LevelBuilder.TOP);
    }

    static final int[] TILE_IDS = {0x302f, 0x3030};
}
