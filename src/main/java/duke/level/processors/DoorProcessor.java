package duke.level.processors;

import duke.gameplay.active.Door;
import duke.gameplay.active.items.Key;
import duke.level.Level;
import duke.level.LevelBuilder;

import java.util.Map;

import static duke.level.Level.TILE_SIZE;
import static duke.level.LevelBuilder.LEFT;

public class DoorProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return DOOR_TILE_IDS.containsKey(tileId);
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        int rows = 0; // TODO duplicate with forcefield; maybe move this to LevelBuilder
        while (canProcess(builder.getTileId(index + (rows * Level.WIDTH)))) {
            builder.replaceTile(index + (rows * Level.WIDTH), LEFT);

            rows++;
        }

        builder.add(new Door(x, y, (rows * TILE_SIZE), DOOR_TILE_IDS.get(tileId)));
    }

    static final Map<Integer, Key.Type> DOOR_TILE_IDS = Map.of(
            0x304c, Key.Type.RED,
            0x304d, Key.Type.GREEN,
            0x304e, Key.Type.BLUE,
            0x304f, Key.Type.MAGENTA
    );
}
