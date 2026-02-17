package duke.state;

import duke.GameContext;
import duke.gfx.Sprite;

public class Credits implements GameState {
    private Sprite image;

    @Override
    public void start(GameContext context) {
        image = context.getAssets().getImage("CREDITS");
    }

    @Override
    public void render(GameContext context) {
        context.getRenderer().draw(image, 0, 0);
    }
}
