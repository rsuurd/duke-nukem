package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class Camera extends Active {
    private int facing;

    public Camera(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(GameState state) {
        int col = (state.getDuke().getX() + 8) / TILE_SIZE;

        facing = Integer.signum(col - getX() / TILE_SIZE);
    }

    @Override
    public boolean canBeShot() {
        return true;
    }

    @Override
    public void hit(GameState state) {
        state.increaseScore(100);
        state.addEffect(new Effect.Score(x, y, 100));
        state.addEffect(new Effect.Sparks(x, y));

        active = false;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getAnim(208 + facing + 1), x, y);
    }
}
