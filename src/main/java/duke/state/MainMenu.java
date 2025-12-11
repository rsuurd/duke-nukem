package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.gfx.Sprite;

public class MainMenu implements GameState {
    @Override
    public void render(GameContext context) {
        Sprite background = context.getAssets().getImage("DN");
        Renderer renderer = context.getRenderer();

        renderer.draw(background, 0, 0);
    }
}
