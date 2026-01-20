package duke.gameplay.active.enemies.behavior;

import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.Enemy;

import static duke.level.Level.TILE_SIZE;

public class PatrolBehavior implements EnemyBehavior {
    private int tick;
    private int interval;
    private int speed;

    public PatrolBehavior(int interval, int speed) {
        this.interval = interval;
        this.speed = speed;
    }

    @Override
    public void behave(WorldQuery worldQuery, Enemy enemy) {
        if (shouldBehave()) {
            if (isEdgeReached(worldQuery, enemy)) {
                turnAround(enemy);
            }

            move(enemy);
        }

        tick = (tick + 1) % interval;
    }

    private boolean shouldBehave() {
        return tick == 0;
    }

    private void turnAround(Enemy enemy) {
        enemy.turnAround();
    }

    private void move(Enemy enemy) {
        int deltaX = (enemy.getFacing() == Facing.RIGHT) ? speed : -speed;

        enemy.setX(enemy.getX() + deltaX);
    }

    private boolean isEdgeReached(WorldQuery query, Enemy enemy) {
        int row = enemy.getRow();
        int col = ((enemy.getFacing() == Facing.RIGHT) ? enemy.getX() + enemy.getWidth() + 1 : enemy.getX() - 1) / TILE_SIZE;

        return query.isSolid(row, col) || !query.isSolid(row + 1, col);
    }
}
