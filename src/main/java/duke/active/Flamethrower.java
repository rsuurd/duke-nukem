package duke.active;

import duke.Assets;
import duke.Facing;
import duke.GameState;
import duke.Renderer;

import static duke.Gfx.TILE_SIZE;

public class Flamethrower extends Active {
    private static final int IGNITION = 32;
    private static final int BURN = 42;
    private static final int DURATION = 62;

    private Facing facing;

    private int timer;

    public Flamethrower(int x, int y, Facing facing) {
        super(x, y);

        this.facing = facing;

        width = (3 * TILE_SIZE) - 1;
    }

    @Override
    public void update(GameState state) {
        timer = (timer + 1) % DURATION;

        int originX = (facing == Facing.RIGHT) ? x : x - (2 * TILE_SIZE);
        if (isBurning() && state.getDuke().collidesWith(originX, y, width, height)) {
            state.getDuke().hurt();
        }
    }

    private boolean isIgniting() {
        return (timer >= IGNITION) && (timer < BURN);
    }

    private boolean isBurning() {
        return timer >= BURN;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        if (isIgniting() && ((timer % 2) == 0)) {
            int tileIndex = (facing == Facing.RIGHT) ? 24 : 29;

            renderer.drawTile(assets.getObject(tileIndex), x, y);
        } else if (isBurning()) {
            int tileIndex = (facing == Facing.RIGHT) ? 25 : 30;
            int deltaX = (facing == Facing.RIGHT) ? TILE_SIZE : -TILE_SIZE;

            renderer.drawTile(assets.getObject(tileIndex + (timer % 2)), x, y);
            renderer.drawTile(assets.getObject(tileIndex + ((timer + 1) % 2)), x + deltaX, y);
            renderer.drawTile(assets.getObject(tileIndex + 2 + (timer % 2)), x + (2 * deltaX), y);
        }
    }
}
