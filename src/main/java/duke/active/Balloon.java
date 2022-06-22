package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class Balloon extends Item {
    private boolean popped;

    public Balloon(int x, int y) {
        super(x, y, 69, 10000);

        popped = false;
    }

    @Override
    public void update(GameState state) {
        if (popped) {
            state.addEffect(new Effect.Smoke(x, y));
            active = false;
        } else {
            velocityY = -1;

            super.update(state);
        }

        frame = (frame + 1) % 12;
    }

    @Override
    protected void bump() {
        popped = true;
    }

    @Override
    public boolean canBeShot() {
        return true;
    }

    @Override
    public void hit(GameState state) {
        popped = true;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(popped ? 73 : 69 ), x, y);
        renderer.drawTile(assets.getObject(70 + (frame / 4)), x, y + TILE_SIZE);
    }
}
