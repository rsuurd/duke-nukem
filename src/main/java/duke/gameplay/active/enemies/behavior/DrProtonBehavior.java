package duke.gameplay.active.enemies.behavior;

import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.Enemy;
import duke.gameplay.player.Player;

import java.util.Random;

import static duke.level.Level.TILE_SIZE;

public class DrProtonBehavior implements EnemyBehavior {
    private Random random;

    private int horizontalAcceleration;
    private int verticalAcceleration;

    private int thinking;
    private int shootTime;

    public DrProtonBehavior() {
        this(new Random());
    }

    DrProtonBehavior(Random random) {
        this.random = random;

        reset();
    }

    private void reset() {
        horizontalAcceleration = 0;
        verticalAcceleration = 0;
        thinking = 0;
        shootTime = random.nextInt(SWOOP_TIME);
    }

    @Override
    public void behave(WorldQuery worldQuery, Enemy enemy) {
        facePlayer(worldQuery.getPlayer(), enemy);
        think(worldQuery, enemy);
    }

    private void facePlayer(Player player, Enemy enemy) {
        int enemyCenterX = enemy.getX() + (enemy.getWidth() / 2);
        int playerCenterX = player.getX() + (player.getWidth() / 2);

        if (enemyCenterX < playerCenterX) {
            enemy.setFacing(Facing.RIGHT);
        } else {
            enemy.setFacing(Facing.LEFT);
        }
    }

    private void think(WorldQuery worldQuery, Enemy enemy) {
        thinking++;

        if (thinking == shootTime) {
            shoot(worldQuery, enemy);
        } else if (thinking == HOVER_TIME) {
            startSwoop(enemy);
        } else if (thinking == HOVER_TIME + SWOOP_TIME) {
            reset();
        }

        updateHorizontalVelocity(enemy);
        updateVerticalVelocity(enemy);
    }

    private void shoot(WorldQuery worldQuery, Enemy enemy) {
        if (canSeePlayer(worldQuery.getPlayer(), enemy)) {
            enemy.shoot();
        }
    }

    private boolean canSeePlayer(Player player, Enemy enemy) {

        int horizontalDistance = Math.abs((enemy.getX() + enemy.getWidth() / 2) - (player.getX() + player.getWidth() / 2));
        int verticalDistance = Math.abs((enemy.getY() + enemy.getHeight() / 2) - (player.getY() + player.getHeight() / 2));
        System.err.println("Can see playa? " + (horizontalDistance < SHOOTING_RANGE && verticalDistance < SHOOTING_RANGE));

        return horizontalDistance < SHOOTING_RANGE && verticalDistance < SHOOTING_RANGE;
    }

    private void startSwoop(Enemy enemy) {
        // swoop down
        horizontalAcceleration = (enemy.getFacing() == Facing.RIGHT) ? HORIZONTAL_ACCELERATION : -HORIZONTAL_ACCELERATION;
        verticalAcceleration = VERTICAL_ACCELERATION;
    }

    private void updateHorizontalVelocity(Enemy enemy) {
        enemy.setVelocityX(enemy.getVelocityX() + horizontalAcceleration);

        if (enemy.getVelocityX() <= -MAX_SPEED || enemy.getVelocityX() >= MAX_SPEED) {
            horizontalAcceleration = -horizontalAcceleration;
        }
    }

    private void updateVerticalVelocity(Enemy enemy) {
        enemy.setVelocityY(enemy.getVelocityY() + verticalAcceleration);

        if (enemy.getVelocityY() <= -MAX_SPEED || enemy.getVelocityY() >= MAX_SPEED) {
            verticalAcceleration = -verticalAcceleration;
        }
    }

    static final int SHOOTING_RANGE = 8 * TILE_SIZE;

    static final int HORIZONTAL_ACCELERATION = 2;
    static final int VERTICAL_ACCELERATION = HORIZONTAL_ACCELERATION * 2;
    static final int MAX_SPEED = TILE_SIZE;

    static final int HOVER_TIME = 16;
    static final int SWOOP_TIME = 16;
}
