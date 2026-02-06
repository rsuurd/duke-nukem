package duke.level.processors;

import duke.gameplay.active.enemies.SnakeBot;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.LEFT;

public class SnakeBotProcessor implements ActiveProcessor {
    static final int TILE_ID = 0x3013;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        for (int i = 0; i < SnakeBot.SIZE; i++) {
            builder.add(new SnakeBot(x, y, i)).replaceTile(index, LEFT);
        }
    }
}
