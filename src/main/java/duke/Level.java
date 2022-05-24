package duke;

import java.awt.image.BufferedImage;

import static duke.Gfx.TILE_SIZE;

public class Level {
    public static final int WIDTH = 128;
    public static final int HEIGHT = 90;

    private int[] tiles;
    private BufferedImage backdrop;

    public Level(int[] tiles, BufferedImage backdrop) {
        this.tiles = tiles;

        this.backdrop = backdrop;
    }

    public int getTile(int row, int col) {
        return ((row >= 0) && (row < HEIGHT) && (col >= 0) && (col < WIDTH)) ? tiles[row * WIDTH + col] : 0;
    }

    public BufferedImage getBackdrop() {
        return backdrop;
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
}
