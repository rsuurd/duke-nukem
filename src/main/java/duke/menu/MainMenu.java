package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.state.*;
import duke.ui.KeyHandler;

import static java.awt.event.KeyEvent.*;

public class MainMenu implements Menu {
    private StateTransitioner transitioner;

    @Override
    public void open(GameSystems systems) {
        transitioner = new StateTransitioner(systems);

        systems.getDialogManager().open(DIALOG);
    }

    @Override
    public void update(GameSystems systems) {
        KeyHandler keys = systems.getKeyHandler();

        if (keys.consume(VK_S)) {
            startNewGame();
        }

        if (keys.consume(VK_I)) {
            showInstructions(systems);
        }

        if (keys.consume(VK_G)) {
            showGameSetup(systems);
        }

        if (keys.consume(VK_H)) {
            showHighScores(systems);
        }

        if (keys.consume(VK_T)) {
            showTitleScreen();
        }

        if (keys.consume(VK_C)) {
            showCredits();
        }

        transitioner.update();
    }

    private void startNewGame() {
        transitioner.requestState(new Prologue());
    }

    private void showInstructions(GameSystems systems) {
        // NOTE this shows "the story" instead
        systems.getMenuManager().open(new Instructions(), systems);
    }

    private void showGameSetup(GameSystems systems) {
        systems.getMenuManager().open(new GameSetup(), systems);
    }

    private void showHighScores(GameSystems systems) {
        systems.getMenuManager().open(new HighScores(), systems);
    }

    private void showTitleScreen() {
        transitioner.requestState(new TitleScreen());
    }

    private void showCredits() {
        transitioner.requestState(new Credits());
    }

    private static final Dialog DIALOG = new Dialog("""
            
                 DUKE MAIN MENU
                 --------------
            
              S)tart a new game
              R)estore an old game
              I)nstructions
              O)rdering information
              G)ame setup
              H)igh scores
              P)review/Main Demo!
              V)iew user demo
              T)itle screen
              C)redits
              Q)uit to DOS
            """, 56, 32, 9, 13, false, false);

    // TODO extract this to a component that supports fade to black and white -> perform an action -> fade to normal
    private static class StateTransitioner implements StateRequester {
        private GameSystems systems;
        private GameState next;

        public StateTransitioner(GameSystems systems) {
            this.systems = systems;
        }

        @Override
        public void requestState(GameState state) {
            next = state;

            systems.getPalette().fadeOut();
        }

        public void update() {
            if (systems.getPalette().isFadedBack() && next != null) {
                systems.getMenuManager().closeAll(systems);

                systems.requestState(next);
            }
        }
    }
}
