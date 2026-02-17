package duke.state;

import duke.GameSystems;

public interface GameState {
    default void start(GameSystems systems) {
    }

    default void update(GameSystems systems) {
    }

    default void render(GameSystems systems) {
    }

    default void stop(GameSystems systems) {
    }
}
