package duke.state;

import duke.GameSystems;
import duke.Renderer;
import duke.dialog.Dialog;
import duke.gfx.Sprite;

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
        }

        systems.getDialogManager().update(systems);
        // TODO if no input for a while, go to credits/demo mode
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
