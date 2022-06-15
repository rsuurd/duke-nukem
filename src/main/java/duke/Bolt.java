package duke;

import duke.active.Active;
import duke.effects.Effect;

import java.awt.image.BufferedImage;

import static duke.Gfx.TILE_SIZE;

public class Bolt extends Active {
    private Facing facing;

    private int frame;

    public Bolt(int x, int y, Facing facing) {
        super(x, y);

        this.facing = facing;

        frame = -1;
    }

    @Override
    public void update(GameState state) {
        x += switch (facing) {
            case LEFT -> -TILE_SIZE;
            case RIGHT -> TILE_SIZE;
        };

        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        if ((x % TILE_SIZE > 0) && (facing == Facing.LEFT)) {
            col += 1;
        }

        if (state.getLevel().isSolid(row, col)) {
            hit();

            state.addEffect(new Effect.Sparks(x, y));
        } else {
            active = Math.abs(x - state.getDuke().getX()) < (10 * TILE_SIZE);
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

    public void hit() {
        active = false;
    }
}
