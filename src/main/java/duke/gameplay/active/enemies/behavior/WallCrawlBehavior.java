package duke.gameplay.active.enemies.behavior;

import duke.gameplay.Active;
import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.WallCrawler;

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
    public void behave(WorldQuery worldQuery, Active active) {
        if (shouldBehave()) {
            if (isEdgeReached(worldQuery, active)) {
                turnAround();

                if (active instanceof WallCrawler wallCrawler) {
                    wallCrawler.reverse();
                }
            }

            move(active);
        }

        tick = (tick + 1) % interval;
    }

    private boolean shouldBehave() {
        return tick == 0;
    }

    private void turnAround() {
        speed = -speed;
    }

    private void move(Active active) {
        active.setY(active.getY() + speed);
    }

    private boolean isEdgeReached(WorldQuery query, Active active) {
        int row = (active.getY() + (isGoingDown() ? active.getHeight() + 1 : -1)) / TILE_SIZE;
        int col = active.getCol();
        return query.isSolid(row, col) || !query.isSolid(row, col + gapOffset);
    }

    private boolean isGoingDown() {
        return speed > 0;
    }
}