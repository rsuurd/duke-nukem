package duke.level.processors;

import duke.gameplay.active.enemies.Techbot;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.LEFT;

public class TechbotProcessor implements ActiveProcessor {
    public static final int TILE_ID = 0x3010;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new Techbot(x, y)).replaceTile(index, LEFT);
    }
}
