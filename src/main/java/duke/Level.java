package duke;

import duke.active.Active;

import java.awt.image.BufferedImage;
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
                int tileId = getTile(row, col);

                collides |= (tileId >= 0x1800) && (tileId <= 0x2FFF);
            }
        }

        return collides;
    }

    public int getPlayerStartX() {
        return (startLocation % WIDTH) * TILE_SIZE;
    }

    public int getPlayerStartY() {
        return (startLocation / WIDTH) * TILE_SIZE;
    }

    public void update(GameState state) {
        getActives().stream().filter(Active::isActive).forEach(active -> active.update(state));
    }
}
