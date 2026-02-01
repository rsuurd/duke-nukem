package duke.level.processors;

import duke.gameplay.active.enemies.FlyingBot;
import duke.level.Level;
import duke.level.LevelBuilder;

public class FlyingBotProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder.add(new FlyingBot(Level.toX(index), Level.toY(index))).replaceTile(index, LevelBuilder.LEFT);
    }

    static final int TILE_ID = 0x300b;
}
