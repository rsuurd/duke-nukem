package duke.level.processors;

import duke.gameplay.active.Door;
import duke.gameplay.active.items.Key;
import duke.level.Level;
import duke.level.LevelBuilder;

import java.util.Map;

public class DoorProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return DOOR_TILE_IDS.containsKey(tileId);
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder.add(new Door(Level.toX(index), Level.toY(index), DOOR_TILE_IDS.get(tileId))).replaceTile(index, LevelBuilder.LEFT);
    }

    static final Map<Integer, Key.Type> DOOR_TILE_IDS = Map.of(
            0x304c, Key.Type.RED,
            0x304d, Key.Type.GREEN,
            0x304e, Key.Type.BLUE,
            0x304f, Key.Type.MAGENTA
    );
}
