package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;

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

        checkHit(state);
    }

    @Override
    protected void hit(GameState state) {
        state.increaseScore(100);

        active = false;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getAnim(208 + facing + 1), x, y);
    }
}
