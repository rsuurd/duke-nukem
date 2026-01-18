package duke.gameplay.active.enemies;

import duke.gameplay.Active;
import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;

import static duke.level.Level.TILE_SIZE;

public class PatrolBehavior implements EnemyBehavior {
    private Facing facing; // TODO direction (up / down / left / right)
    private int tick;
    private int interval;
    private int speed;

    public PatrolBehavior(Facing facing, int interval, int speed) {
        this.facing = facing;
        this.interval = interval;
        this.speed = speed;
    }

    @Override
    public void behave(WorldQuery worldQuery, Active active) {
        if (shouldBehave()) {
            if (isEdgeReached(worldQuery, active)) {
                turnAround();
            }

            move(active);
        }

        tick = (tick + 1) % interval;
    }

    private boolean shouldBehave() {
        return tick == 0;
    }

    private void turnAround() {
        facing = facing == Facing.LEFT ? Facing.RIGHT : Facing.LEFT;
    }

    private void move(Active active) {
        int deltaX = (facing == Facing.RIGHT) ? speed : -speed;

        active.setX(active.getX() + deltaX);
    }

    private boolean isEdgeReached(WorldQuery query, Active active) {
        int row = active.getRow();
        int col = ((facing == Facing.RIGHT) ? active.getX() + active.getWidth() + 1 : active.getX() - 1) / TILE_SIZE;

        return query.isSolid(row, col) || !query.isSolid(row + 1, col);
    }

    public Facing getFacing() {
        return facing;
    }
}
