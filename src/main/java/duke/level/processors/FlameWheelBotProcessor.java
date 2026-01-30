package duke.level.processors;

import duke.gameplay.active.enemies.FlameWheelBot;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.LEFT;

public class FlameWheelBotProcessor implements ActiveProcessor {
    static final int TILE_ID = 0x300e;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new FlameWheelBot(x, y)).replaceTile(index, LEFT);
    }
}
