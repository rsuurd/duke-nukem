package duke.gameplay;

public interface Collidable {
    void onCollision(Direction direction, int flags);

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
