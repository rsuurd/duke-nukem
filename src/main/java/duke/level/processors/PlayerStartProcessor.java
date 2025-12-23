package duke.level.processors;

import duke.level.LevelBuilder;

public class PlayerStartProcessor implements ActiveProcessor {
    public static final int TILE_ID = 0x3032;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder
                .playerStart(index)
                .replaceTile(index, LevelBuilder.LEFT);
    }
}
