package duke;

import duke.gfx.EgaPalette;
import duke.resources.ResourceLoader;
import duke.state.GameState;
import duke.ui.KeyHandler;

public class GameContext {
    private ResourceLoader resourceLoader;
    private Renderer renderer;
    private EgaPalette palette; // maybe combine renderer/palette in Gfx abstraction
    private KeyHandler keyHandler;

    // TODO probably need a StateManager around this later
    private GameState gameState;

    public GameContext(ResourceLoader resourceLoader, Renderer renderer, EgaPalette palette, KeyHandler keyHandler, GameState gameState) {
        this.resourceLoader = resourceLoader;
        this.renderer = renderer;
        this.palette = palette;
        this.keyHandler = keyHandler;
        this.gameState = gameState;
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

    public GameState getGameState() {
        return gameState;
    }
}
