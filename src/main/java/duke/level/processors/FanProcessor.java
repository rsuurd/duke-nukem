package duke.level.processors;

import duke.gameplay.Facing;
import duke.gameplay.active.Fan;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.Level.TILE_SIZE;
import static duke.level.LevelBuilder.LEFT;
import static duke.level.LevelBuilder.RIGHT;

public class FanProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == FAN_LEFT || tileId == FAN_RIGHT;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index) - TILE_SIZE;

        Facing facing = switch (tileId) {
            case FAN_LEFT -> Facing.LEFT;
            case FAN_RIGHT -> Facing.RIGHT;
            default -> null;
        };

        builder.add(new Fan(x, y, facing)).replaceTile(index, (facing == Facing.LEFT) ? LEFT : RIGHT);
    }

    static final int FAN_LEFT = 0x301b;
    static final int FAN_RIGHT = 0x301c;
}
