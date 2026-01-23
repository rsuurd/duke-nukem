package duke.level.processors;

import duke.gameplay.active.Notes;
import duke.level.Level;
import duke.level.LevelBuilder;

public class NotesProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == TILE_ID;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder.add(new Notes(Level.toX(index), Level.toY(index)));
    }

    static final int TILE_ID = 0x3040;
}
