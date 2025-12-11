package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.gfx.Sprite;

public class MainMenu implements GameState {
    private Sprite background;

    @Override
    public void start(GameContext context) {
        background = context.getResourceLoader().getSpriteLoader().readImage("DN.DN1");
    }

    @Override
    public void render(GameContext context) {
        Renderer renderer = context.getRenderer();

        renderer.draw(background, 0, 0);
    }
}
