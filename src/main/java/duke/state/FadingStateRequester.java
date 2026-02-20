package duke.state;

import duke.GameSystems;
import duke.gfx.EgaPalette;

public class FadingStateRequester implements StateRequester {
    private StateManager manager;
    private EgaPalette palette;

    private GameState next;
    private Transition transition;

    public FadingStateRequester(StateManager manager, EgaPalette palette) {
        this.manager = manager;
        this.palette = palette;
    }

    @Override
    public void requestState(GameState state, Transition transition) {
        if (next != null) {
            throw new IllegalStateException("Already requesting a state change");
        }

        next = state;
        this.transition = transition;

        switch (transition) {
            case FADE_OUT -> palette.fadeOut();
        }
    }

    public void update(GameSystems systems) {
        if (next == null) return;

        switch (transition) {
            case NONE -> setNextState(systems);
            case FADE_OUT -> fadeOut(systems);
        }

    }

    private void setNextState(GameSystems systems) {
        manager.set(next, systems);

        next = null;
    }

    private void fadeOut(GameSystems systems) {
        if (palette.isFadedBack()) {
            manager.set(next, systems);

            palette.fadeIn();
        }

        if (palette.isFadedIn()) {
            next = null;
        }
    }
}
