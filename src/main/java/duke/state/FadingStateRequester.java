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
    public boolean isTransitioning() {
        return next != null;
    }

    @Override
    public void requestState(GameState state, Transition transition) {
        if (isTransitioning()) {
            throw new IllegalStateException("Already requesting a state change");
        }

        next = state;
        this.transition = transition;

        switch (transition) {
            case FADE_TO_BLACK -> palette.fadeOut();
            case FADE_TO_WHITE -> palette.fadeToWhite();
        }
    }

    public void update(GameSystems systems) {
        if (next == null) return;

        switch (transition) {
            case NONE -> setNextState(systems);
            case FADE_TO_BLACK -> fadeToBlack(systems);
            case FADE_TO_WHITE -> fadeToWhite(systems);
        }
    }

    private void setNextState(GameSystems systems) {
        manager.set(next, systems);

        next = null;
    }

    private void fadeToBlack(GameSystems systems) {
        if (palette.isFadedBlack()) {
            manager.set(next, systems);

            palette.fadeIn();
        }

        if (palette.isFadedIn()) {
            next = null;
        }
    }

    private void fadeToWhite(GameSystems systems) {
        if (palette.isFadedWhite()) {
            manager.set(next, systems);

            palette.fadeFromWhite();
        }

        if (palette.isFadedIn()) {
            next = null;
        }
    }
}
