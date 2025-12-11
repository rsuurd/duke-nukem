package duke.level;

public class LevelData {
    private static final int WIDTH = 128;
    private static final int HEIGHT = 90;

    private int[] data;

    public LevelData(int[] data) {
        if (data.length != WIDTH * HEIGHT) {
            throw new IllegalArgumentException("Unexpected level size");
        }

        this.data = data;
    }

    public int getTile(int row, int col) {
        return data[row * WIDTH + col];
    }
}
