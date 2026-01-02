package duke;

import duke.gfx.EgaPalette;
import duke.resources.AssetManager;
import duke.sfx.SoundManager;
import duke.ui.KeyHandler;

public class GameContext {
    private AssetManager assets;
    private Renderer renderer;
    private EgaPalette palette; // maybe combine renderer/palette in Gfx abstraction
    private KeyHandler keyHandler;
    private SoundManager soundManager;

    public GameContext(AssetManager assets, Renderer renderer, EgaPalette palette, KeyHandler keyHandler, SoundManager soundManager) {
        this.assets = assets;
        this.renderer = renderer;
        this.palette = palette;
        this.keyHandler = keyHandler;
        this.soundManager = soundManager;
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
}
