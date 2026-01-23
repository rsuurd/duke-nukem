package duke.level;

import duke.gameplay.Active;
import duke.level.processors.ActiveProcessorRegistry;

import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
    public static final int TOP = -Level.WIDTH;
    public static final int BOTTOM = Level.WIDTH;
    public static final int LEFT = -1;
    public static final int RIGHT = 1;

    private ActiveProcessorRegistry registry;

    private LevelDescriptor descriptor;
    private int[] data;
    private int playerStart;
    private List<Active> actives;

    public LevelBuilder(ActiveProcessorRegistry registry, LevelDescriptor descriptor, int[] data) {
        this.registry = registry;
        this.descriptor = descriptor;
        this.data = data;

        actives = new ArrayList<>();
    }

    public LevelBuilder playerStart(int playerStart) {
        this.playerStart = playerStart;
        return this;
    }

    public LevelBuilder replaceTile(int index, int offset) {
        data[index] = data[index + offset];

        return this;
    }

    public int getTileId(int index) {
        return data[index];
    }

    public LevelBuilder add(Active active) {
        actives.add(active);

        return this;
    }

    public Level build() {
        for (int i = 0; i < data.length; i++) {
            int tileId = data[i];

            // anything below 0x3000 is never an active
            if (tileId >= Level.ACTIVE) {
                registry.getProcessor(tileId).process(i, tileId, this);
            }
        }

        return new Level(descriptor, data, playerStart, actives);
    }
}
