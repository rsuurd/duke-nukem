package duke;

import duke.active.Active;
import duke.effects.Effect;

import java.awt.image.BufferedImage;

import static duke.Gfx.TILE_SIZE;

public class Bolt extends Active {
    private Facing facing;

    private int frame;

    private boolean hitSomething;

    public Bolt(int x, int y, Facing facing) {
        super(x, y);

        this.facing = facing;

        frame = -1;
    }

    @Override
    public void update(GameState state) {
        if (hitSomething) {
            active = false;
        } else {
            Level level = state.getLevel();

            moveTo(x + ((facing == Facing.LEFT) ? -TILE_SIZE : TILE_SIZE), y);

            int i = 0;

            while (!hitSomething && (i < level.getActives().size())) {
                Active active = level.getActives().get(i++);

                hitSomething = active.canBeShot() && active.collidesWith(this);

                if (hitSomething) {
                    active.hit(state);
                }
            }

            int row = y / TILE_SIZE;
            int col = (x + ((facing == Facing.LEFT) ? x % TILE_SIZE : 0)) / TILE_SIZE;

            if (level.isSolid(row, col)) {
                int sparkX = x + ((facing == Facing.LEFT) ? 8 : -8);

                state.addEffect(new Effect.Sparks(sparkX, y - 4));

                hitWall(state);
            } else {
                active = Math.abs(x - state.getDuke().getX()) < (10 * TILE_SIZE);
            }
        }
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        BufferedImage sprite;

        if (frame == -1) {
            sprite = assets.getObject((facing == Facing.LEFT) ? 46 : 47);
        } else {
            sprite = assets.getObject(6 + frame);
        }

        renderer.drawTile(sprite, x, y - 8);

        frame = (frame + 1) % 4;
    }

    @Override
    protected void hitWall(GameState state) {
        active = false;
    }
}
