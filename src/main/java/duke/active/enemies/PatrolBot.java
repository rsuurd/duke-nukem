package duke.active.enemies;

import duke.Facing;
import duke.GameState;
import duke.active.Active;

import static duke.Gfx.TILE_SIZE;

class PatrolBot extends Active {
    protected Facing facing;
    private int speed;

    PatrolBot(int x, int y, Facing facing, int speed) {
        super(x, y);

        this.facing = facing;
        this.speed = speed;
    }

    @Override
    public void update(GameState state) {
        patrol(state);

        super.update(state);

        if (state.getDuke().collidesWith(this)) {
            state.getDuke().hurt();
        }
    }

    protected void patrol(GameState state) {
        if (isEdgeReached(state)) {
            reverse();
        }

        velocityX = (facing == Facing.LEFT) ? -speed : speed;
    }

    private boolean isEdgeReached(GameState state) {
        int row = y / TILE_SIZE;
        int nextCol = (x + ((facing == Facing.LEFT) ? -speed : speed + width)) / TILE_SIZE;

        boolean blocked = state.getLevel().isSolid(row, nextCol);
        boolean pit = !state.getLevel().isSolid(row + 1, nextCol);

        return blocked || pit;
    }

    private void reverse() {
        facing = (facing == Facing.LEFT) ? Facing.RIGHT : Facing.LEFT;
    }

    @Override
    public boolean canBeShot() {
        return true;
    }
}
