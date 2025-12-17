package duke.level;

import duke.level.processors.ActiveProcessorRegistry;

public class LevelBuilder {
    private ActiveProcessorRegistry registry;

    private int number;
    private int[] data;
    private int backdrop;
    private int playerStart;

    public LevelBuilder(ActiveProcessorRegistry registry, int number, int[] data) {
        this.registry = registry;
        this.number = number;
        this.data = data;
    }

    public LevelBuilder playerStart(int playerStart) {
        this.playerStart = playerStart;
        return this;
    }

    // TODO maybe just do like replaceTile(idx, left | up | right | down, and let the builder calculate source index
    public void replaceTile(int index, int sourceIndex) {
        data[index] = data[sourceIndex];
    }

    public Level build() {
        determineBackdrop();

        for (int i = 0; i < data.length; i++) {
            int tileId = data[i];

            // anything below 0x3000 is never an active
            if (tileId >= Level.ACTIVE) {
                registry.getProcessor(tileId).process(i, tileId, this);
            }
        }

        return new Level(number, data, backdrop, playerStart);
    }

    private void determineBackdrop() {
        // backdrops are hardcoded depending on level number

        this.backdrop = 0;
    }
}
