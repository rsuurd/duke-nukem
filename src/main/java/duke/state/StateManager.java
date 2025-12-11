package duke.state;

import duke.GameContext;

public class StateManager {
    private GameContext context;
    private GameState state;

    public StateManager(GameContext context, GameState state) {
        this.context = context;

        this.state = state;
        this.state.start(context);
    }

    public void set(GameState state) {
        this.state.stop(context);
        this.state = state;
        this.state.start(context);
    }

    public void update() {
        state.update(context);
    }

    public void render() {
        state.render(context);
    }
}
