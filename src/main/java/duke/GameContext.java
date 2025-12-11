package duke;

import duke.gfx.EgaPalette;
import duke.resources.ResourceLoader;
import duke.ui.KeyHandler;

public class GameContext {
    private ResourceLoader resourceLoader;
    private Renderer renderer;
    private EgaPalette palette; // maybe combine renderer/palette in Gfx abstraction
    private KeyHandler keyHandler;

    public GameContext(ResourceLoader resourceLoader, Renderer renderer, EgaPalette palette, KeyHandler keyHandler) {
        this.resourceLoader = resourceLoader;
        this.renderer = renderer;
        this.palette = palette;
        this.keyHandler = keyHandler;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
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
