package duke.state;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.Cheats;
import duke.gfx.EgaPalette;
import duke.gfx.Sprite;

import java.util.List;

public class Prologue implements GameState {
    private int chapter;

    private Sprite drProton;
    private Sprite duke;
    private Dialog dialog;

    @Override
    public void start(GameSystems systems) {
        chapter = 0;

        drProton = systems.getAssets().getImage("BADGUY");
        duke = systems.getAssets().getImage("DUKE");

        systems.getPalette().blackout();
    }

    @Override
    public void update(GameSystems systems) {
        EgaPalette palette = systems.getPalette();

        if (palette.isFadedOut()) {
            systems.getDialogManager().close();
            palette.fadeIn();

            if (hasNextChapter()) {
                dialog = STORY.get(chapter++);
            } else {
                // TODO move later to palette.isFadedWhite() to trigger state change
                systems.requestState(new GameplayState(systems, new Cheats(true)));

                return;
            }
        }

        if (palette.isFadedIn() && !systems.getDialogManager().hasDialog()) {
            systems.getDialogManager().open(dialog);
        }

        if (systems.getKeyHandler().isAnyKeyPressed()) {
            palette.fadeOut();
        }
    }

    private boolean hasNextChapter() {
        return chapter < STORY.size();
    }

    @Override
    public void render(GameSystems systems) {
        // should probably get background from the story, but we just have 2 storyboards
        systems.getRenderer().draw(chapter == 1 ? drProton : duke, 0, 0);
        systems.getDialogManager().render(systems.getRenderer());
    }

    private static final List<Dialog> STORY = List.of(
            new Dialog("So you're the pitiful\nhero they sent to stop\nme.  I, Dr. Proton, will\nsoon rule the world!", 16, 144, 3, 13, true, false),
            new Dialog("You're wrong, Proton\nbreath.  I'll be done\nwith you and still have\ntime to watch Oprah!", 112, 144, 3, 13, true, false)
    );
}
