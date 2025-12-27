package duke.gameplay;

import duke.level.Level;

public interface Physics extends Movable {
    int GRAVITY = 2;

    int getVerticalAcceleration();

    default int getTerminalVelocity() {
        return Level.TILE_SIZE;
    }

    void fall();
}
