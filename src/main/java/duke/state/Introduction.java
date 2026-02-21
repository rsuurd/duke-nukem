package duke.state;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gfx.EgaPalette;

import java.util.Iterator;
import java.util.List;

public class Introduction implements GameState {
    private Iterator<Dialog> dialogs;

    private int countdown;

    @Override
    public void start(GameSystems systems) {
        systems.getPalette().blackout();

        dialogs = List.of(LOADING, CREDITS).iterator();
    }

    @Override
    public void update(GameSystems systems) {
        EgaPalette palette = systems.getPalette();

        if (systems.getPalette().isFadedBlack()) {
            if (dialogs.hasNext()) {
                showDialog(systems, dialogs.next());
            } else {
                switchToTitleScreen(systems);

                return;
            }
        }

        if (systems.getPalette().isFadedIn()) {
            if (countdown == 0) {
                palette.fadeOut();
            } else {
                countdown--;
            }
        }
    }

    private void showDialog(GameSystems systems, Dialog dialog) {
        systems.getDialogManager().close();
        systems.getDialogManager().open(dialog);
        systems.getPalette().fadeIn();
        countdown = COUNTDOWN;
    }

    private void switchToTitleScreen(GameSystems systems) {
        systems.getDialogManager().close();

        systems.getStateRequester().requestState(new TitleScreen());
    }

    @Override
    public void render(GameSystems systems) {
        // We can maybe hook in dialog rendering as part of the game loop after the state renders
        // since it's most likely the same for every state
        systems.getDialogManager().render(systems.getRenderer());
    }

    private static final Dialog LOADING = new Dialog("   Loading Duke Nukum,\n      please wait...", 56, 80, 2, 13, false, false);
    private static final Dialog CREDITS = new Dialog("""
            
                 APOGEE SOFTWARE
                   PRODUCTIONS
                      - - -
            
                    A game by
                  Todd Replogle
            
                   Version 2.0
            
               Copyright  (c) 1991
            """, 56, 48, 7, 13, false, false);

    static final int COUNTDOWN = 16;
}
