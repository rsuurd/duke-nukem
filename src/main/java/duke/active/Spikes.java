package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;

public class Spikes extends Active {
    private boolean up;

    public Spikes(int x, int y, boolean up) {
        super(x, y);

        this.up = up;
    }

    @Override
    public void update(GameState state) {
        if (state.getDuke().collidesWith(this)) {
            state.getDuke().hurt();
        }
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        int tileIndex = up ? 148 : 149;

        renderer.drawTile(assets.getObject(tileIndex), x, y);
    }
}
