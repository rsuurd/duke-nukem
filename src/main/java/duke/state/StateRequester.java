package duke.state;

import duke.GameSystems;

public interface StateRequester {
    default void requestState(GameState state) {
        requestState(state, Transition.FADE_OUT);
    }

    void requestState(GameState state, Transition transition);

    void update(GameSystems systems);

    enum Transition {
        NONE,
        FADE_OUT
    }
}
