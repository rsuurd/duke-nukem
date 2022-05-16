package duke;

public class Level {
    public static final int WIDTH = 128;
    public static final int HEIGHT = 90;

    private int[] tiles;

    public Level(int[] tiles) {
        this.tiles = tiles;
    }

    public int getTile(int row, int col) {
        return tiles[row * WIDTH + col];
    }
}
