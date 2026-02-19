package duke.state;

import duke.GameSystems;
import duke.Renderer;
import duke.dialog.Dialog;
import duke.gfx.Sprite;

import static java.awt.event.KeyEvent.VK_S;

public class TitleScreen implements GameState {
    private Sprite background;

    @Override
    public void start(GameSystems systems) {
        background = systems.getAssets().getImage("DN");
        systems.getPalette().fadeIn();
    }

    @Override
    public void update(GameSystems systems) {
        if (systems.getKeyHandler().isAnyKeyPressed() && !systems.getDialogManager().hasDialog()) {
            systems.getDialogManager().open(DIALOG);
        } else {
            // state transition requested, but only execute once faded out
            if (next != null) {
                if (systems.getPalette().isFadedBack()) {
                    systems.requestState(next);
                }
            } else {
                switch (systems.getKeyHandler().getPressedKey()) {
                    case VK_S -> fadeToState(systems, new Prologue());
                    // other keystrokes
                }
            }
        }
    }

    private GameState next;

    private void fadeToState(GameSystems systems, GameState state) {
        next = state;
        systems.getPalette().fadeOut();
    }

    @Override
    public void render(GameSystems systems) {
        Renderer renderer = systems.getRenderer();

        renderer.draw(background, 0, 0);

        systems.getDialogManager().render(systems.getRenderer());
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
}
