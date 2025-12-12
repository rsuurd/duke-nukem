package duke.level.processors;

import duke.level.LevelBuilder;

public interface ActiveProcessor {
    void process(int index, int tileId, LevelBuilder builder);
}
