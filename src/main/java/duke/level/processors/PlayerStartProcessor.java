package duke.level.processors;

import duke.level.LevelBuilder;

public class PlayerStartProcessor implements ActiveProcessor {
    public static final int TILE_ID = 0x3032;

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder
                .playerStart(index)
                .replaceTile(index, index - 1);
    }
}
