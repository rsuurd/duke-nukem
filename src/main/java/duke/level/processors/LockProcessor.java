package duke.level.processors;

import duke.gameplay.active.Lock;
import duke.gameplay.active.items.Key;
import duke.level.Level;
import duke.level.LevelBuilder;

import java.util.Map;

public class LockProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return LOCK_TILE_IDS.containsKey(tileId);
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder.add(new Lock(Level.toX(index), Level.toY(index), LOCK_TILE_IDS.get(tileId)));
    }

    static final Map<Integer, Key.Type> LOCK_TILE_IDS = Map.of(
            0x3048, Key.Type.RED,
            0x3049, Key.Type.GREEN,
            0x304a, Key.Type.BLUE,
            0x304b, Key.Type.MAGENTA
    );
}
