package duke.active.enemies;

import duke.Assets;
import duke.Facing;
import duke.GameState;
import duke.Renderer;
import duke.active.Active;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class WallCrawler extends Active {
    private static final int SPEED = 1;

    private Facing facing;
    private boolean up;

    private int frame;

    public WallCrawler(int x, int y, Facing facing) {
        super(x, y);

        this.facing = facing;
        up = true;

        frame = 0;
    }

    @Override
    protected void applyGravity() {}

    @Override
    public void update(GameState state) {
        if (isEdgeReached(state)) {
            reverse();
        }

        velocityY = up ? -SPEED : SPEED;

        super.update(state);

        if (state.getDuke().collidesWith(this)) {
            state.getDuke().hurt();
        }
    }

    private boolean isEdgeReached(GameState state) {
        int nextRow = (y + (up ? -SPEED : SPEED + height)) / TILE_SIZE;
        int col = x / TILE_SIZE;

        boolean blocked = state.getLevel().isSolid(nextRow, col);
        boolean pit = !state.getLevel().isSolid(nextRow, col + ((facing == Facing.LEFT) ? 1 : -1));

        return blocked || pit;
    }

    @Override
    protected void bump() {
        reverse();
    }

    @Override
    protected void land(GameState state) {
        reverse();
    }

    private void reverse() {
        up = !up;
    }

    @Override
    public boolean canBeShot() {
        return true;
    }

    @Override
    public void hit(GameState state) {
        state.increaseScore(100);
        state.addEffect(new Effect.Smoke(x, y));

        active = false;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        int tileIndex = (facing == Facing.RIGHT) ? 140 : 144;

        renderer.drawTile(assets.getAnim(tileIndex + frame), x ,y);

        frame = ((frame + (up ? 1 : -1)) + 4) % 4;
    }
}
