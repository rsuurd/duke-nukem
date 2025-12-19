package duke.gameplay;

public interface Collidable {
    void onCollision(Direction direction);

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
