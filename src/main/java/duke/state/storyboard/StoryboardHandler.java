package duke.state.storyboard;

import duke.GameSystems;
import duke.gfx.EgaPalette;
import duke.state.GameState;
import duke.state.StateRequester;

public class StoryboardHandler {
    private Storyboard storyboard;

    private Step step;

    private GameState next;
    private StateRequester.Transition transition;

    public StoryboardHandler(Storyboard storyboard, GameState next, StateRequester.Transition transition) {
        this.storyboard = storyboard;

        this.step = Step.BEGIN;

        this.next = next;
        this.transition = transition;
    }

    public void update(GameSystems systems) {
        switch (step) {
            case BEGIN -> beginPanel(systems);
            case FADING_IN -> showDialogsWhenReady(systems);
            case SHOWING_PANEL -> closePanelWhenRequested(systems);
            case FADING_OUT -> advanceStoryWhenReady(systems);
        }
    }

    private void beginPanel(GameSystems systems) {
        if (storyboard.current().getSfx() != null) {
            systems.getSoundManager().play(storyboard.current().getSfx());
        }

        systems.getPalette().fadeIn();
        step = Step.FADING_IN;
    }

    private void showDialogsWhenReady(GameSystems systems) {
        EgaPalette palette = systems.getPalette();

        if (palette.isFadedIn()) {
            if (storyboard.current().getDialog() != null) {
                systems.getDialogManager().open(storyboard.current().getDialog());
            }

            step = Step.SHOWING_PANEL;
        }
    }

    private void closePanelWhenRequested(GameSystems systems) {
        if (systems.getKeyHandler().consumeAny()) {
            if (storyboard.hasNext()) {
                systems.getPalette().fadeOut();
                step = Step.FADING_OUT;
            } else {
                systems.getStateRequester().requestState(next, transition);
                step = Step.FINISHED;
            }
        }
    }

    private void advanceStoryWhenReady(GameSystems systems) {
        if (systems.getPalette().isFadedBlack()) {
            storyboard.advance();
            systems.getDialogManager().close();
            step = Step.BEGIN;
        }
    }

    public void render(GameSystems systems) {
        Panel panel = storyboard.current();

        if (panel.getImage() != null) {
            systems.getRenderer().draw(panel.getImage(), 0, 0);
        }

        systems.getDialogManager().render(systems.getRenderer());
    }

    enum Step {
        BEGIN,
        FADING_IN,
        SHOWING_PANEL,
        FADING_OUT,
        FINISHED
    }
}
