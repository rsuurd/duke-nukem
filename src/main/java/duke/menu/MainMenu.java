package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.state.Credits;
import duke.state.Prologue;
import duke.state.TextPages;
import duke.ui.KeyHandler;

import static java.awt.event.KeyEvent.*;

public class MainMenu implements Menu {
    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(DIALOG);
    }

    @Override
    public void update(GameSystems systems) {
        KeyHandler keys = systems.getKeyHandler();

        if (keys.consume(VK_S)) {
            startNewGame(systems);
        }

        if (keys.consume(VK_R)) {
            restoreGame(systems);
        }

        if (keys.consume(VK_I)) {
            showInstructions(systems);
        }

        if (keys.consume(VK_O)) {
            showOrderingInformation(systems);
        }

        if (keys.consume(VK_G)) {
            showGameSetup(systems);
        }

        if (keys.consume(VK_H)) {
            showHighScores(systems);
        }

        if (keys.consume(VK_T)) {
            showTitleScreen(systems);
        }

        if (keys.consume(VK_C)) {
            showCredits(systems);
        }
    }

    private void startNewGame(GameSystems systems) {
        systems.getStateRequester().requestState(new Prologue());
    }

    private void restoreGame(GameSystems systems) {
        systems.getMenuManager().open(new RestoreGame(), systems);
    }

    private void showInstructions(GameSystems systems) {
        systems.getStateRequester().requestState(new TextPages("instructions"));
    }

    private void showOrderingInformation(GameSystems systems) {
        systems.getStateRequester().requestState(new TextPages("ordering-information"));
    }

    private void showGameSetup(GameSystems systems) {
        systems.getMenuManager().open(new GameSetup(MAIN_MENU_X), systems);
    }

    private void showHighScores(GameSystems systems) {
        systems.getMenuManager().open(new HighScores(MAIN_MENU_X), systems);
    }

    private void showTitleScreen(GameSystems systems) {
        systems.getStateRequester().requestState(new Prologue());
    }

    private void showCredits(GameSystems systems) {
        systems.getStateRequester().requestState(new Credits());
    }

    static final int MAIN_MENU_X = 56;

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
            """, MAIN_MENU_X, 32, 9, 13, false, false);


}
