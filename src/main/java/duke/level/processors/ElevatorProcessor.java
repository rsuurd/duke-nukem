package duke.level.processors;

import duke.gameplay.active.Elevator;
import duke.level.Level;
import duke.level.LevelBuilder;

public class ElevatorProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return tileId == 0x3001;
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder.add(new Elevator(Level.toX(index), Level.toY(index)));
    }
}
