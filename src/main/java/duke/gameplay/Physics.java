package duke.gameplay;

import duke.level.Level;

public interface Physics extends Movable {
    int GRAVITY = 2;

    default int getVerticalAcceleration() {
        return Level.HALF_TILE_SIZE;
    }

    default int getTerminalVelocity() {
        return Level.TILE_SIZE;
    }

    default void fall() {
    }
}
