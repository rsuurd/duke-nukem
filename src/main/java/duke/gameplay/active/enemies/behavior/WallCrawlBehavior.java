package duke.gameplay.active.enemies.behavior;

import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.Enemy;

import static duke.level.Level.TILE_SIZE;

public class WallCrawlBehavior implements EnemyBehavior {
    private int tick;
    private int interval;
    private int speed;
    private int gapOffset;

    public WallCrawlBehavior(Facing facing, int interval, int speed) {
        this.interval = interval;
        this.speed = -speed;
        gapOffset = facing == Facing.RIGHT ? -1 : 1;
    }

    @Override
    public void behave(WorldQuery worldQuery, Enemy enemy) {
        if (shouldBehave()) {
            if (isEdgeReached(worldQuery, enemy)) {
                reverse(enemy);
            }

            move(enemy);
        }

        tick = (tick + 1) % interval;
    }

    private boolean shouldBehave() {
        return tick == 0;
    }

    private void reverse(Enemy enemy) {
        enemy.reverse();
        speed = -speed;
    }

    private void move(Enemy enemy) {
        enemy.setY(enemy.getY() + speed);
    }

    private boolean isEdgeReached(WorldQuery query, Enemy enemy) {
        int row = (enemy.getY() + (isGoingDown() ? enemy.getHeight() + 1 : -1)) / TILE_SIZE;
        int col = enemy.getCol();
        return query.isSolid(row, col) || !query.isSolid(row, col + gapOffset);
    }

    private boolean isGoingDown() {
        return speed > 0;
    }
}
