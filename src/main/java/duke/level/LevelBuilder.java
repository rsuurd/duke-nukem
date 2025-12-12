package duke.level;

public class LevelBuilder {
    private int number;
    private int[] data;
    private int backdrop;

    public LevelBuilder(int number, int[] data) {
        this.number = number;
        this.data = data;
    }

    public Level build() {
        // iterate tiles and build entities, player start, tiles etc
        
        return new Level(number, data, backdrop);
    }
}
