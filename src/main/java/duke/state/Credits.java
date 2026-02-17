package duke.state;

import duke.GameSystems;
import duke.gfx.Sprite;

public class Credits implements GameState {
    private Sprite image;

    @Override
    public void start(GameSystems systems) {
        image = systems.getAssets().getImage("CREDITS");
    }

    @Override
    public void render(GameSystems systems) {
        systems.getRenderer().draw(image, 0, 0);
    }
}
