package duke.level;

import duke.gameplay.Active;
import duke.level.processors.ActiveProcessorRegistry;

import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
    private ActiveProcessorRegistry registry;

    private int number;
    private int[] data;
    private int backdrop;
    private int playerStart;
    private List<Active> actives;

    public LevelBuilder(ActiveProcessorRegistry registry, int number, int[] data) {
        this.registry = registry;
        this.number = number;
        this.data = data;

        actives = new ArrayList<>();
    }

    public LevelBuilder playerStart(int playerStart) {
        this.playerStart = playerStart;
        return this;
    }

    // TODO maybe just do like replaceTile(idx, left | up | right | down, and let the builder calculate source index
    public void replaceTile(int index, int sourceIndex) {
        data[index] = data[sourceIndex];
    }

    public LevelBuilder add(Active active) {
        actives.add(active);

        return this;
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

        return new Level(number, data, backdrop, playerStart, actives);
    }

    private void determineBackdrop() {
        // backdrops are hardcoded depending on level number

        this.backdrop = 0;
    }
}
