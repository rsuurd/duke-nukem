package duke.active.enemies;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.active.Active;

public class Mine extends Active {
    public Mine(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(GameState state) {
        super.update(state);

        if (state.getDuke().collidesWith(this)) {
            state.spawn(new Explosion(x, y));

            active = false;
        }
    }

    @Override
    public boolean canBeShot() {
        return true;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getAnim(231), x, y);
    }
}
