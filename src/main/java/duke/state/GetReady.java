package duke.state;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.SaveGame;
import duke.gfx.Font;
import duke.gfx.Hud;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class GetReady implements GameState {
    private SaveGame saveGame;

    private Hud hud;

    // TODO optional
    private int countdown;

    public GetReady() {
        this(null);
    }

    public GetReady(SaveGame saveGame) {
        this.saveGame = saveGame;
    }

    @Override
    public void start(GameSystems systems) {
        systems.getPalette().fadeFromWhite();

        hud = new Hud(systems.getAssets(), new Font(systems.getAssets()));

        systems.getSoundManager().play(Sfx.START_GAME);
        systems.getDialogManager().open(GET_READY_DIALOG);

        countdown = COUNTDOWN;
    }

    @Override
    public void update(GameSystems systems) {
        if (countdown-- <= 0) {
            systems.getStateRequester().requestState(new GameplayState(saveGame), StateRequester.Transition.NONE);
        }
    }

    @Override
    public void render(GameSystems systems) {
        hud.render(systems.getRenderer());
        systems.getDialogManager().render(systems.getRenderer());
    }

    static final int COUNTDOWN = 64;

    private static final Dialog GET_READY_DIALOG = new Dialog("""
                 Get ready Duke,
                you're going in!!
            """, TILE_SIZE, 80, 2, 13, false, false);
}
