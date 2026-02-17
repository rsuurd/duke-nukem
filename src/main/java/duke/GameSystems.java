package duke;

import duke.dialog.DialogManager;
import duke.gfx.EgaPalette;
import duke.resources.AssetManager;
import duke.sfx.SoundManager;
import duke.state.GameState;
import duke.state.StateManager;
import duke.state.StateRequester;
import duke.ui.KeyHandler;

public class GameSystems implements StateRequester {
    private AssetManager assets;
    private Renderer renderer;
    private EgaPalette palette; // maybe combine renderer/palette in Gfx abstraction
    private KeyHandler keyHandler;
    private SoundManager soundManager;

    private StateManager stateManager;
    private DialogManager dialogManager;

    public GameSystems(AssetManager assets, Renderer renderer, EgaPalette palette, KeyHandler keyHandler, SoundManager soundManager, StateManager stateManager, DialogManager dialogManager) {
        this.assets = assets;
        this.renderer = renderer;
        this.palette = palette;
        this.keyHandler = keyHandler;
        this.soundManager = soundManager;
        this.stateManager = stateManager;
        this.dialogManager = dialogManager;
    }

    public AssetManager getAssets() {
        return assets;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public EgaPalette getPalette() {
        return palette;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    @Override
    public void requestState(GameState state) {
        stateManager.set(state, this);
    }

    public DialogManager getDialogManager() {
        return dialogManager;
    }
}
