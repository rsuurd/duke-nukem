package duke.state;

import duke.GameSystems;

public interface StateRequester {
    default void requestState(GameState state) {
        requestState(state, Transition.FADE_TO_BLACK);
    }

    void requestState(GameState state, Transition transition);

    void update(GameSystems systems);

    boolean isTransitioning();

    enum Transition {
        NONE,
        FADE_TO_BLACK,
        FADE_TO_WHITE
    }
}
