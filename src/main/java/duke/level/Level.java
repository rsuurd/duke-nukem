package duke.level;

public class Level {
    static final int WIDTH = 128;
    static final int HEIGHT = 90;

    private int number;
    private int[] tiles;
    private int backdrop;

    // player start location
    // initial set of entities

    public Level(int number, int[] tiles, int backdrop) {
        if (tiles.length != WIDTH * HEIGHT) {
            throw new IllegalArgumentException("Unexpected level size");
        }

        this.number = number;
        this.tiles = tiles;
        this.backdrop = backdrop;
    }

    public int getNumber() {
        return number;
    }

    public int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public int getTile(int row, int col) {
        // check bounds
        return tiles[row * WIDTH + col];
    }

    public int getBackdrop() {
        return backdrop;
    }
}
