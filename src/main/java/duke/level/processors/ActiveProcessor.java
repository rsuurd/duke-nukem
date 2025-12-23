package duke.level.processors;

import duke.level.LevelBuilder;

public interface ActiveProcessor {
    boolean canProcess(int tileId);

    void process(int index, int tileId, LevelBuilder builder);
}
