package duke.level;

import duke.gameplay.Active;

import java.util.Collections;
import java.util.List;

public class Level {
    public static final int TILE_SIZE = 16;
    public static final int FLASHERS = 0x0;
    public static final int BACKGROUNDS = 0x600;
    public static final int SOLIDS = 0x1800;
    public static final int ACTIVE = 0x3000;
    public static final int HACKS = 0x306A;

    public static final int WIDTH = 128;
    public static final int HEIGHT = 90;

    private int number;
    private int[] tiles;
    private int backdrop;
    private int playerStart;

    private List<Active> actives;

    public Level(int number, int[] tiles, int backdrop, int playerStart, List<Active> actives) {
        if (tiles.length != WIDTH * HEIGHT) {
            throw new IllegalArgumentException("Unexpected level size");
        }

        this.number = number;
        this.tiles = tiles;
        this.backdrop = backdrop;
        this.playerStart = playerStart;
        this.actives = actives;
    }

    public int getNumber() {
        return number;
    }

    public int getPlayerStartX() {
        return (playerStart % WIDTH) * TILE_SIZE;
    }

    public int getPlayerStartY() {
        return (playerStart / WIDTH) * TILE_SIZE;
    }

    public List<Active> getActives() {
        return Collections.unmodifiableList(actives);
    }

    public int getTile(int row, int col) {
        if (row < 0 || row >= HEIGHT || col < 0 || col >= WIDTH) {
            return 0;
        }

        return tiles[row * WIDTH + col];
    }

    public int getBackdrop() {
        return backdrop;
    }

    public boolean isSolid(int row, int col) {
        int tileId = getTile(row, col);

        return (tileId >= SOLIDS) && (tileId < ACTIVE);
    }
}
