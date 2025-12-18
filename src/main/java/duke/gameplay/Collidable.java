package duke.gameplay;

public interface Collidable {
    void onCollide(Direction direction);

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
