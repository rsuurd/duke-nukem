package duke.active.enemies;

import duke.Assets;
import duke.Facing;
import duke.GameState;
import duke.Renderer;
import duke.active.Active;

public class Shot extends Active {
    private Facing facing;

    private int frame;

    public Shot(int x, int y, Facing facing) {
        super(x, y);

        this.facing = facing;

        frame = -1;
    }

    @Override
    protected void applyGravity() {}

    @Override
    public void update(GameState state) {
        velocityX = (facing == Facing.LEFT) ? -8 : 8;

        super.update(state);

        if (state.getDuke().collidesWith(this)) {
            state.getDuke().hurt();

            active = false;
        }
    }

    @Override
    protected void hitWall(GameState state) {
        active = false;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        int tileId;

        if (frame == -1) {
            tileId = ((facing == Facing.LEFT)) ? 48 : 49;
        } else {
            tileId = ((facing == Facing.LEFT) ? 39 : 41) + frame;
        }

        renderer.drawTile(assets.getObject(tileId), x, y);

        frame = (frame + 1) % 2;
    }
}
