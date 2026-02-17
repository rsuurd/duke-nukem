package duke.state;

import duke.GameSystems;
import duke.Renderer;
import duke.dialog.Dialog;
import duke.gfx.Sprite;

public class MainMenu implements GameState {
    private Sprite background;

    @Override
    public void start(GameSystems systems) {
        systems.getDialogManager().open(DIALOG);

        background = systems.getAssets().getImage("DN");
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
