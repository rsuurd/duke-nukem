package duke.level.processors;

import duke.gameplay.Facing;
import duke.gameplay.active.enemies.WallCrawler;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.LEFT;
import static duke.level.LevelBuilder.RIGHT;

public class WallCrawlerProcessor implements ActiveProcessor {
    static final int TILE_ID_RIGHT = 0x3016;
    static final int TILE_ID_LEFT = 0x3017;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID_RIGHT || tileId == TILE_ID_LEFT;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        Facing facing = (tileId == TILE_ID_RIGHT) ? Facing.RIGHT : Facing.LEFT;
        int tileOffset = (tileId == TILE_ID_RIGHT) ? RIGHT : LEFT;

        builder.add(new WallCrawler(x, y, facing)).replaceTile(index, tileOffset);
    }

}
