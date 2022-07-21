package duke.active;

import duke.Assets;
import duke.Duke;
import duke.GameState;
import duke.Renderer;

public class Needle extends Active {
    private boolean poking;

    public Needle(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(GameState state) {
        Duke duke = state.getDuke();

        poking = duke.collidesWith(this);

        if (poking) {
            duke.hurt();
        }
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        int tileIndex = poking ? 96 : 95;

        renderer.drawTile(assets.getObject(tileIndex), x, y);
    }
}
