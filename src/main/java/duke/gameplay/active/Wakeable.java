package duke.gameplay.active;

public interface Wakeable {
    default boolean isAwake() {
        return true;
    }

    default void wakeUp() {}
}
