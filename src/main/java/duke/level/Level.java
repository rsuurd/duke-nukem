package duke.level;

import duke.gameplay.Active;

import java.util.Collections;
import java.util.List;

public class Level {
    public static final int TILE_SIZE = 16;
    public static final int HALF_TILE_SIZE = TILE_SIZE / 2;
    public static final int FLASHERS = 0x80;
    public static final int BACKGROUNDS = 0x600;
    public static final int SOLIDS = 0x1800;
    public static final int CONVEYOR = 0x1b80;
    public static final int CONVEYOR_END = 0x1ca0;
    public static final int CLINGABLE = 0x2ee0;
    public static final int ACTIVE = 0x3000;
    public static final int HACKS = 0x306A;

    public static final int WIDTH = 128;
    public static final int HEIGHT = 90;

    private LevelDescriptor descriptor;
    private int[] tiles;
    private int playerStart;

    private List<Active> actives;

    private boolean completed;

    public Level(LevelDescriptor descriptor, int[] tiles, int playerStart, List<Active> actives) {
        if (tiles.length != WIDTH * HEIGHT) {
            throw new IllegalArgumentException("Unexpected level size");
        }

        this.descriptor = descriptor;
        this.tiles = tiles;
        this.playerStart = playerStart;
        this.actives = actives;
    }

    public LevelDescriptor getDescriptor() {
        return descriptor;
    }

    public int getPlayerStartX() {
        return toX(playerStart);
    }

    public int getPlayerStartY() {
        return toY(playerStart) - TILE_SIZE;
    }

    public List<Active> getActives() {
        return Collections.unmodifiableList(actives);
    }

    public int getTile(int row, int col) {
        if (isInBounds(row, col)) {
            return tiles[row * WIDTH + col];
        } else {
            return 0;
        }
    }

    public int getTileFlags(int row, int col) {
        int flags = 0;
        int tileId = getTile(row, col);

        if (isSolid(tileId)) {
            flags |= Flags.SOLID.bit();
        }

        if (isClingable(tileId)) {
            flags |= Flags.CLINGABLE.bit();
        }

        return flags;
    }

    public void setTile(int row, int col, int tileId) {
        if (isInBounds(row, col)) {
            tiles[row * WIDTH + col] = tileId;
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < HEIGHT && col >= 0 && col < WIDTH;
    }

    public static int toX(int address) {
        return (address % WIDTH) * TILE_SIZE;
    }

    public static int toY(int address) {
        return (address / WIDTH) * TILE_SIZE;
    }

    public static boolean isSolid(int tileId) {
        return (tileId >= SOLIDS) && (tileId < ACTIVE);
    }

    private static boolean isClingable(int tileId) {
        return (tileId >= CONVEYOR && tileId <= CONVEYOR_END) || (tileId >= CLINGABLE && tileId < ACTIVE);
    }

    public void complete() {
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }
}
