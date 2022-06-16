package duke.active.enemies;

import duke.Assets;
import duke.Facing;
import duke.GameState;
import duke.Renderer;
import duke.active.Active;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class Techbot extends Active {
    private static final int SPEED = 8;
    private static final int SIZE = TILE_SIZE - 1;
    private static final int DEATH_TIMER = 7;

    private Facing facing;

    private int frame;
    private int exploding;

    public Techbot(int x, int y) {
        super(x, y);

        facing = Facing.RIGHT;
        frame = 0;
        exploding = -1;
    }

    @Override
    public void update(GameState state) {
        if (isAlive()) {
            if (frame == 0) {
                if (isEdgeReached(state)) {
                    reverse();
                }

                velocityX = (facing == Facing.LEFT) ? -SPEED : SPEED;
            }

            super.update(state);

            if (state.getDuke().collidesWith(this)) {
                state.getDuke().hurt();
            }

            checkHit(state);
        } else {
            if (exploding == DEATH_TIMER) {
                state.increaseScore(100);
                state.addEffect(new Effect.Sparks(x, y));

                active = false;
            }
        }
    }

    @Override
    protected void hitWall() {
        reverse();
    }

    private void reverse() {
        facing = (facing == Facing.LEFT) ? Facing.RIGHT : Facing.LEFT;
    }

    private boolean isEdgeReached(GameState state) {
        int row = y / TILE_SIZE;
        int nextCol = (x + ((facing == Facing.LEFT) ? -SPEED : SPEED + width)) / TILE_SIZE;

        boolean blocked = state.getLevel().isSolid(row, nextCol);
        boolean pit = !state.getLevel().isSolid(row + 1, nextCol);

        return blocked || pit;
    }

    private boolean isAlive() {
        return exploding < 0;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        if (isAlive()) {
            renderer.drawTile(assets.getAnim(82 + frame), x, y);

            frame = (frame + 1) % 3;
        } else {
            renderer.drawTile(assets.getAnim(85 + exploding), x, y);

            exploding ++;
        }
    }

    protected void hit(GameState state) {
        exploding = 0;
    }
}
