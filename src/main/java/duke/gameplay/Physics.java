package duke.gameplay;

public interface Physics extends Movable {
    int GRAVITY = 2;

    int getVerticalAcceleration();
    int getTerminalVelocity();
    void fall();
}
