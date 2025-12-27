package duke.level.processors;

import duke.gameplay.active.SecurityCamera;
import duke.level.Level;
import duke.level.LevelBuilder;

import static duke.level.LevelBuilder.BOTTOM;

public class SecurityCameraProcessor implements ActiveProcessor {
    public static final int TILE_ID = 0x3024;

    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        builder.add(new SecurityCamera(x, y)).replaceTile(index, BOTTOM);
    }
}
