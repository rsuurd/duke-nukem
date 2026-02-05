package duke.gameplay.active.enemies.behavior;

import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.Enemy;
import duke.gameplay.player.Player;

public class HelicopterBehavior implements EnemyBehavior {
    private int flightTime;

    @Override
    public void behave(WorldQuery worldQuery, Enemy enemy) {
        calculateFlightPlan(worldQuery, enemy);
    }

    private void calculateFlightPlan(WorldQuery worldQuery, Enemy enemy) {
        if (--flightTime <= 0) {
            flightTime = FLIGHT_TIME;
            enemy.setVelocityX(getVelocityX(worldQuery, enemy));
            enemy.setVelocityY(getVelocityY(worldQuery, enemy));
        }
    }

    private int getVelocityX(WorldQuery worldQuery, Enemy enemy) {
        Player player = worldQuery.getPlayer();

        if (player.getX() < enemy.getX()) {
            return -SPEED;
        } else if (player.getX() + player.getWidth() > enemy.getX() + enemy.getWidth()) {
            return SPEED;
        } else {
            return 0;
        }
    }

    private int getVelocityY(WorldQuery worldQuery, Enemy enemy) {
        if (shouldCrash(enemy)) {
            return CRASH_SPEED;
        } else if (shouldDescend(worldQuery, enemy)) {
            return DESCEND_SPEED;
        } else if (shouldAscend(worldQuery, enemy)) {
            return -SPEED;
        } else {
            return 0;
        }
    }

    private boolean shouldCrash(Enemy enemy) {
        return enemy.getHealth().isDead();
    }

    private boolean shouldAscend(WorldQuery worldQuery, Enemy enemy) {
        return enemy.getY() > worldQuery.getPlayer().getY() || isSolidBelow(worldQuery, enemy);
    }

    private boolean isSolidBelow(WorldQuery worldQuery, Enemy enemy) {
        // TODO maybe introduce a bit of slack here, 2 is the exact chopper height
        return worldQuery.isSolid(enemy.getRow() + 2, enemy.getCol());
    }

    private boolean shouldDescend(WorldQuery worldQuery, Enemy enemy) {
        return enemy.getY() < worldQuery.getPlayer().getY();
    }

    static final int SPEED = 4;
    static final int DESCEND_SPEED = SPEED / 2;
    static final int CRASH_SPEED = DESCEND_SPEED * 2;
    static final int FLIGHT_TIME = 8;
}
