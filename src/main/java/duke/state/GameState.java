package duke.state;

import duke.GameContext;

public interface GameState {
    default void start(GameContext context) {
    }

    default void update(GameContext context) {
    }

    default void render(GameContext context) {
    }

    default void stop(GameContext context) {
    }
}
