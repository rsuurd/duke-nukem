package duke;

import duke.active.Active;

import java.awt.*;

import static duke.Gfx.TILE_SIZE;

public class Bolt extends Active {
    private Facing facing;

    private int frame;

    private Rectangle hitBox;

    public Bolt(int x, int y, Facing facing) {
        super(x, y);

        this.facing = facing;

        hitBox = new Rectangle(x, y + 8, 15, 7);
    }

    @Override
    public void update(GameState state) {
        x += switch (facing) {
            case LEFT -> -TILE_SIZE;
            case RIGHT -> TILE_SIZE;
        };

        hitBox.x = x;

        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        if ((x % TILE_SIZE > 0) && (facing == Facing.LEFT)) {
            col += 1;
        }

        int tileId = state.getLevel().getTile(row, col);
        boolean solid = (tileId >= 0x1800) && (tileId <= 0x2FFF);

        if (solid) {
            hit();
        } else {
            active = Math.abs(x - state.getDuke().getX()) < (10 * TILE_SIZE);
        }

    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(6 + frame), x, y);

        frame = (frame + 1) % 4;
    }

    public boolean hits(Active active) {
        Rectangle hitBox = new Rectangle(active.getX(), active.getY(), 15, 15); // TODO active.getHitBox();

        return this.hitBox.intersects(hitBox);
    }

    public void hit() {
        active = false;
    }
}
