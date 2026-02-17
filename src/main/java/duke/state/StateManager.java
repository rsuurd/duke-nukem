package duke.state;

import duke.GameSystems;

public class StateManager {
    private GameState state;

    public StateManager() {
        this(null);
    }

    StateManager(GameState state) {
        this.state = state;
    }

    public void set(GameState state, GameSystems systems) {
        if (this.state != null) {
            this.state.stop(systems);
        }

        this.state = state;
        this.state.start(systems);
    }

    public GameState get() {
        return state;
    }
}
