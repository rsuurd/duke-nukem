package duke.level.processors;

import duke.gameplay.active.enemies.Tankbot;
import duke.gameplay.active.enemies.Techbot;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.LEFT;

public class TankbotProcessor implements ActiveProcessor {
    public static final int TILE_ID = 0x300d;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new Tankbot(x, y)).replaceTile(index, LEFT);
    }
}
