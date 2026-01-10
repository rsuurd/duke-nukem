package duke.level.processors;

import duke.gameplay.active.Spikes;
import duke.level.Level;
import duke.level.LevelBuilder;

public class SpikesProcessor implements ActiveProcessor {
    static final int SPIKES_UP_TILE_ID = 0x3058;
    static final int SPIKES_DOWN_TILE_ID = 0x3059;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == SPIKES_UP_TILE_ID || tileId == SPIKES_DOWN_TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        if (tileId == SPIKES_UP_TILE_ID) {
            builder.add(new Spikes(x, y, Spikes.Direction.UP)).replaceTile(index, LevelBuilder.TOP);
        } else if (tileId == SPIKES_DOWN_TILE_ID) {
            builder.add(new Spikes(x, y, Spikes.Direction.DOWN)).replaceTile(index, LevelBuilder.BOTTOM);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported TileId: 0x%04x", tileId));
        }
    }
}
