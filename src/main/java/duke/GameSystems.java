package duke;

import duke.dialog.DialogManager;
import duke.gfx.EgaPalette;
import duke.menu.MenuManager;
import duke.resources.AssetManager;
import duke.sfx.SoundManager;
import duke.state.StateRequester;
import duke.ui.KeyHandler;

public class GameSystems {
    private AssetManager assets;
    private Renderer renderer;
    private EgaPalette palette; // maybe combine renderer/palette in Gfx abstraction
    private KeyHandler keyHandler;

    private StateRequester stateRequester;
    private SoundManager soundManager;
    private DialogManager dialogManager;
    private MenuManager menuManager;

    public GameSystems(AssetManager assets, Renderer renderer, EgaPalette palette, KeyHandler keyHandler, StateRequester stateRequester, SoundManager soundManager, DialogManager dialogManager, MenuManager menuManager) {
        this.assets = assets;
        this.renderer = renderer;
        this.palette = palette;
        this.keyHandler = keyHandler;
        this.stateRequester = stateRequester;
        this.soundManager = soundManager;
        this.dialogManager = dialogManager;
        this.menuManager = menuManager;
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

    public StateRequester getStateRequester() {
        return stateRequester;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public DialogManager getDialogManager() {
        return dialogManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }
}
