package duke;

import duke.gfx.EgaPalette;
import duke.resources.AssetManager;
import duke.ui.KeyHandler;

public class GameContext {
    private AssetManager assets;
    private Renderer renderer;
    private EgaPalette palette; // maybe combine renderer/palette in Gfx abstraction
    private KeyHandler keyHandler;

    public GameContext(AssetManager assets, Renderer renderer, EgaPalette palette, KeyHandler keyHandler) {
        this.assets = assets;
        this.renderer = renderer;
        this.palette = palette;
        this.keyHandler = keyHandler;
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
}
