package duke;

import duke.active.Active;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import static duke.Gfx.TILE_SIZE;

public class Level {
    public static final int WIDTH = 128;
    public static final int HEIGHT = 90;

    private int[] tiles;
    private int startLocation;
    private BufferedImage backdrop;
    private List<Active> actives;

    public Level(int[] tiles, int startLocation, BufferedImage backdrop, List<Active> actives) {
        this.tiles = tiles;
        this.startLocation = startLocation;
        this.backdrop = backdrop;
        this.actives = actives;
    }

    public int getTile(int row, int col) {
        return ((row >= 0) && (row < HEIGHT) && (col >= 0) && (col < WIDTH)) ? tiles[row * WIDTH + col] : 0;
    }

    public BufferedImage getBackdrop() {
        return backdrop;
    }

    public List<Active> getActives() {
        return actives;
    }

    public boolean collides(int x, int y, int width, int height) {
        boolean collides = false;

        for (int row = y / TILE_SIZE; row <= (y + height) / TILE_SIZE; row ++) {
            for (int col = x / TILE_SIZE; col <= (x + width) / TILE_SIZE; col ++) {
                collides |= isSolid(row, col);
            }
        }

        return collides;
    }

    public boolean isSolid(int row, int col) {
        return isSolid(getTile(row, col));
    }

    public static boolean isSolid(int tileId) {
        return (tileId >= 0x1800) && (tileId <= 0x2FFF);
    }

    public int getPlayerStartX() {
        return (startLocation % WIDTH) * TILE_SIZE;
    }

    public int getPlayerStartY() {
        return (startLocation / WIDTH) * TILE_SIZE;
    }

    public void update(GameState state) {
        Iterator<Active> iterator = getActives().iterator();

        while (iterator.hasNext()) {
            Active active = iterator.next();

            active.update(state);

            if (!active.isActive()) {
                iterator.remove();
            }
        }
    }

    public void setTile(int row, int col, int tileId) {
        tiles[row * Level.WIDTH + col] = tileId;
    }
}
