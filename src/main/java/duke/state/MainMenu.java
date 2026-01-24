package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.dialog.Dialog;
import duke.gfx.Sprite;

public class MainMenu implements GameState {
    @Override
    public void start(GameContext context) {
        context.getDialogManager().open(DIALOG);
    }

    @Override
    public void render(GameContext context) {
        Sprite background = context.getAssets().getImage("DN");
        Renderer renderer = context.getRenderer();

        renderer.draw(background, 0, 0);

        context.getDialogManager().render(context.getRenderer());
    }

    private static final Dialog DIALOG = Dialog.create("""
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
            """, false);
}
