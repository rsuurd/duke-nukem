package duke.level;

public class Level {
    static final int WIDTH = 128;
    static final int HEIGHT = 90;

    private int number;
    private int[] tiles;
    private int backdrop;

    private int playerStart;
    // initial set of entities

    public Level(int number, int[] tiles, int backdrop, int playerStart) {
        if (tiles.length != WIDTH * HEIGHT) {
            throw new IllegalArgumentException("Unexpected level size");
        }

        this.number = number;
        this.tiles = tiles;
        this.backdrop = backdrop;
        this.playerStart = playerStart;
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

    public int getPlayerStartX() {
        return (playerStart % WIDTH) * 16; // TILE_SIZE
    }

    public int getPlayerStartY() {
        return (playerStart / WIDTH) * 16; // TILE_SIZE
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
}
