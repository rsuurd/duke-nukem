package duke.level.processors;

import duke.gameplay.Facing;
import duke.gameplay.active.ConveyorBelt;
import duke.level.Level;
import duke.level.LevelBuilder;

import java.util.List;

import static duke.gameplay.active.ConveyorBelt.SOLID_LEFT_CONVEYOR_TILE_ID;
import static duke.gameplay.active.ConveyorBelt.SOLID_RIGHT_CONVEYOR_TILE_ID;

public class ConveyorBeltProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return TILE_IDS.contains(tileId);
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        // Skip end tiles since we search for them when encountering the start tile
        if (tileId == LEFT_END_TILE_ID || tileId == RIGHT_END_TILE_ID) return;

        Facing direction = getDirection(tileId);
        int cols = calculateConveyorBeltColumns(index, direction, builder);
        int width = (cols + 1) * Level.TILE_SIZE;

        builder
                .setTile(index, SOLID_LEFT_CONVEYOR_TILE_ID)
                .setTile(index + cols, SOLID_RIGHT_CONVEYOR_TILE_ID)
                .add(new ConveyorBelt(Level.toX(index), Level.toY(index), width, direction));
    }

    private int calculateConveyorBeltColumns(int index, Facing direction, LevelBuilder builder) {
        int endTileId = (direction == Facing.LEFT) ? LEFT_END_TILE_ID : RIGHT_END_TILE_ID;
        int cols = 1;
        boolean found = false;

        while (!found && cols < Level.WIDTH) {
            found = builder.getTileId(index + cols) == endTileId;

            if (!found) {
                cols++;
            }
        }

        return cols;
    }

    private Facing getDirection(int tileId) {
        return switch (tileId) {
            case LEFT_START_TILE_ID -> Facing.LEFT;
            case RIGHT_START_TILE_ID -> Facing.RIGHT;
            default -> throw new IllegalArgumentException("Invalid starting tile ID for conveyor belt: " + tileId);
        };
    }

    static final int LEFT_START_TILE_ID = 0x3002;
    static final int LEFT_END_TILE_ID = 0x3003;
    static final int RIGHT_START_TILE_ID = 0x3004;
    static final int RIGHT_END_TILE_ID = 0x3005;

    static final List<Integer> TILE_IDS = List.of(LEFT_START_TILE_ID, LEFT_END_TILE_ID, RIGHT_START_TILE_ID, RIGHT_END_TILE_ID);
}
