package duke.gameplay;

public interface Solid {
    default boolean isSolid() {
        return true;
    }
}
