package duke;

import java.awt.image.BufferedImage;

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
}
