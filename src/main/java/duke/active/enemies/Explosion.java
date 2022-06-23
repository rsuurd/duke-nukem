package duke.active.enemies;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.active.Active;

public class Explosion extends Active {
    private int frame;

    public Explosion(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(GameState state) {
        super.update(state);

        if (state.getDuke().collidesWith(this)) {
            state.getDuke().hurt();
        }

        if (frame >= 6) {
            active = false;
        }
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getAnim(92 + frame), x, y);

        frame++;
    }
}
