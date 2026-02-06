package duke.gameplay.active.enemies.behavior;

import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.Enemy;

import static duke.level.Level.TILE_SIZE;

public class SnakeBotBehavior implements EnemyBehavior {
    private int horizontalSpeed;
    private int horizontalVelocity;

    private int maximumSpeedTime;

    private int verticalSpeed;
    private int verticalVelocity;

    private int patrolTime;
    private int waitTime;

    public SnakeBotBehavior(int waitTime) {
        this(waitTime, PATROL_TIME - 8); // Initial PatrolTime is a bit shorter because we start at an offset
    }

    SnakeBotBehavior(int waitTime, int patrolTime) {
        horizontalSpeed = 0;
        horizontalVelocity = VELOCITY;

        verticalSpeed = -MAX_SPEED;
        verticalVelocity = VELOCITY;

        this.patrolTime = patrolTime;
        this.waitTime = waitTime;
    }

    @Override
    public void behave(WorldQuery worldQuery, Enemy enemy) {
        if (--patrolTime <= 0) {
            patrolTime = PATROL_TIME;
            waitTime = WAIT_TIME;
        }

        if (waitTime-- > 0) return;

        updateHorizontalMotion(enemy);
        updateVerticalMotion(enemy);
    }

    private void updateHorizontalMotion(Enemy enemy) {
        enemy.setX(enemy.getX() + horizontalSpeed);

        horizontalSpeed += horizontalVelocity;

        if (horizontalSpeed < -MAX_SPEED) {
            horizontalSpeed = -MAX_SPEED;
            maximumSpeedTime++;
        } else if (horizontalSpeed > MAX_SPEED) {
            horizontalSpeed = MAX_SPEED;
            maximumSpeedTime++;
        }

        if (maximumSpeedTime >= MAX_SPEED_TIME) {
            horizontalVelocity = -horizontalVelocity;
            maximumSpeedTime = 0;
        }
    }

    private void updateVerticalMotion(Enemy enemy) {
        enemy.setY(enemy.getY() + verticalSpeed);

        verticalSpeed += verticalVelocity;

        if (verticalSpeed <= -MAX_SPEED || verticalSpeed >= MAX_SPEED) {
            verticalVelocity = -verticalVelocity;
        }
    }

    static final int MAX_SPEED = TILE_SIZE;
    static final int VELOCITY = 4;
    static final int PATROL_TIME = 32;
    static final int WAIT_TIME = 16;
    static final int MAX_SPEED_TIME = 8;
}
